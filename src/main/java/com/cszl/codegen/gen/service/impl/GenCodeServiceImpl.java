package com.cszl.codegen.gen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cszl.codegen.base.constants.MetaQuerySql;
import com.cszl.codegen.base.db.DbTemplateFactory;
import com.cszl.codegen.base.enums.DbOperation;
import com.cszl.codegen.base.enums.NamingStrategy;
import com.cszl.codegen.base.utils.BeanUtil;
import com.cszl.codegen.base.utils.CszlJdbcTemplateUtils;
import com.cszl.codegen.base.utils.PropertiesUtils;
import com.cszl.codegen.gen.config.*;
import com.cszl.codegen.gen.entity.mysql.ColumnInfo;
import com.cszl.codegen.gen.enums.GenFileType;
import com.cszl.codegen.gen.enums.QueryConditionRel;
import com.cszl.codegen.gen.enums.QueryRel;
import com.cszl.codegen.gen.service.GenCodeService;
import com.cszl.codegen.gen.strategy.GenMethodStrategy;
import com.cszl.codegen.gen.utils.TemplateUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * create by wdq on 2021/4/10 10:03
 */
@Service
public class GenCodeServiceImpl implements GenCodeService {

    @SneakyThrows
    @Override
    public byte[] gen(GenerateConfig generateConfig, DbTemplateFactory.CszlJdbcTemplate jdbcTemplate) {

        generateConfig.setDataBaseType(jdbcTemplate.getDataSourceConfig().getDataBaseType());

        DbTemplateFactory.CszlJdbcTemplate cszlJdbcTemplate = CszlJdbcTemplateUtils.get();

        List<ColumnInfo> columnInfos = cszlJdbcTemplate.getList(MetaQuerySql.USE_SQL.get(cszlJdbcTemplate.getDataSourceConfig().getDataBaseType()).get(DbOperation.queryColumn), ColumnInfo.class, cszlJdbcTemplate.getDataSourceConfig().getDbName(), generateConfig.getTableName());

        generateConfig.setClassMainName(generateConfig.getNamingStrategy().equals(NamingStrategy.underline_to_camel) ? NamingStrategy.capitalFirst(NamingStrategy.underlineToCamel(generateConfig.getTableName())) : generateConfig.getTableName());

        List<GenField> genFieldList = columnInfos.parallelStream().map(m -> {
            GenField genField = new GenField();
            genField.setColumnName(m.getColumnName());
            genField.setColumnType(m.getDataType());
            genField.setPropertyName(m.getColumnName());
            if (generateConfig.getNamingStrategy().equals(NamingStrategy.underline_to_camel)) {
                genField.setPropertyName(NamingStrategy.underlineToCamel(m.getColumnName()));
            }
            genField.setPropertyType(Optional.ofNullable(PropertiesUtils.typeMapping.getProperty(genField.getColumnType())).orElse("String"));
            genField.setIsPrimaryKey("PRI".equals(m.getColumnKey()));
            genField.setColumnComment(m.getColumnComment());
            genField.setIdType("auto_increment".equals(m.getExtra()) ? "(type = IdType.AUTO)" : "");

            if ("BigDecimal".equals(genField.getPropertyType())) {
                generateConfig.setHasBigDeciaml(true);
            }

            return genField;
        }).collect(Collectors.toList());

        generateConfig.setGenFieldList(genFieldList);

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        VelocityContext vc = new VelocityContext(BeanUtil.beanToMap(generateConfig));
        vc.put("columnsJoin", genFieldList.parallelStream().map(GenField::getColumnName).reduce((a, b) -> a + "," + b).orElse(""));


        //需要创建的文件类型
        List<GenFileType> genFileTypes = generateConfig.getGenFileTypes();

        //字段列表转map
        Map<String, GenField> genFieldColumnMap = genFieldList.stream().collect(Collectors.toMap(GenField::getColumnName, a -> a));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        genFileTypes.forEach(genFileType -> {

            //生成的方法
            vc.put("methods", buildMethods(generateConfig, genFileType, genFieldColumnMap));
            String templatePath = TemplateUtils.getTemplatePath(genFileType);
            StringWriter sw = new StringWriter();
            ve.mergeTemplate(templatePath, Charset.defaultCharset().name(), vc, sw);

            ZipEntry zipEntry = new ZipEntry(getZipEntryPath(genFileType, generateConfig));

            try {
                zipOutputStream.putNextEntry(zipEntry);
                IOUtils.write(sw.toString(), zipOutputStream, Charset.defaultCharset().name());
                IOUtils.closeQuietly(sw);
                zipOutputStream.closeEntry();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        IOUtils.closeQuietly(zipOutputStream);

        return byteArrayOutputStream.toByteArray();

    }

    private String buildMethods(GenerateConfig generateConfig, GenFileType genFileType, Map<String, GenField> genFieldColumnMap) {
        StringBuilder methods = new StringBuilder("");

        List<GenMethod> genMethods = generateConfig.getGenMethodList();

        genMethods.forEach(c -> {

            GenMethodStrategy instance = c.getGenMethodName().getInstance();
            instance.setGenerateConfig(generateConfig);
            instance.setGenFileType(genFileType);
            List<GenQueryField> queryFields = queryXXColumnNameStrToList(c.getQueryXX(), genFieldColumnMap);
            instance.setQueryXX(queryFields);
            List<GenField> updateFields = updateXXColumnNameStrToList(c.getUpdateXX(), genFieldColumnMap);
            instance.setUpdateXX(updateFields);
            String methodsStr = instance.build();
            methods.append(methodsStr);

        });

        return methods.toString();

    }

    /**
     * 获取文件名
     *
     * @param genFileType
     * @param generateConfig
     * @return
     */
    private String getZipEntryPath(GenFileType genFileType, GenerateConfig generateConfig) {
        String fileSeparator = TemplateUtils.FILE_SEPARATOR;
        String name = genFileType.name();
        String ext = name.substring(name.lastIndexOf("_") + 1, name.length());
        return genFileType.pathFun.apply(generateConfig).replace(".", fileSeparator) + fileSeparator + genFileType.nameFun.apply(generateConfig) + "." + ext;
    }

    /**
     * 字符串参数转GenField字段
     *
     * @param list
     * @param genFieldColumnMap
     * @return
     */
    private List<GenField> updateXXColumnNameStrToList(List<String> list, Map<String, GenField> genFieldColumnMap) {
        if (list == null || list.size() == 0) {
            return genFieldColumnMap.values().parallelStream().collect(Collectors.toList());
        }
        return list.parallelStream().map(genFieldColumnMap::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 字符串参数转GenField字段
     *
     * @param list
     * @param genFieldColumnMap
     * @return
     */
    private List<GenQueryField> queryXXColumnNameStrToList(List<QueryXX> list, Map<String, ? extends GenField> genFieldColumnMap) {
        if (CollectionUtils.isEmpty(list)) {
            return genFieldColumnMap.values().parallelStream().map(m -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.putAll(BeanUtil.beanToMap(m));
                GenQueryField queryField = BeanUtil.jsonToBean(jsonObject, new GenQueryField());
                //默认
                queryField.setQueryRel(QueryRel.EQ);
                queryField.setQueryConditionRel(QueryConditionRel.AND);
                return queryField;
            }).collect(Collectors.toList());
        }
        return list.parallelStream().map(m -> {
            GenField genField = genFieldColumnMap.get(m.getName());
            if (genField == null) {
                return null;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.putAll(BeanUtil.beanToMap(genField));
            GenQueryField queryField = BeanUtil.jsonToBean(jsonObject, new GenQueryField());
            queryField.setQueryRel(m.getQueryRel());
            queryField.setQueryConditionRel(m.getQueryConditionRel());
            return queryField;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}

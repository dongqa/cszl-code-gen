package com.cszl.codegen.gen.strategy.methods;

import com.cszl.codegen.base.enums.DataBaseType;
import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.GenMethodStrategy;
import com.cszl.codegen.gen.utils.TemplateUtils;

import java.util.*;
import java.util.function.BiFunction;

/**
 * @author Vick C
 * @version 1.0
 * @date 2022/4/18 17:51
 */
public class GenUpdateByIdImpl extends GenMethodStrategy {

    private final static Map<DataBaseType, BiFunction<GenerateConfig, List<? extends GenField>, String>> strategys = new HashMap<DataBaseType, BiFunction<GenerateConfig, List<? extends GenField>, String>>() {{
        final BiFunction<GenerateConfig, List<? extends GenField>, String> createMysqlIntertIntoXml = GenUpdateByIdImpl::createMysqlUpdateEntityByIdXml;
        final BiFunction<GenerateConfig, List<? extends GenField>, String> createPgIntertIntoXml = GenUpdateByIdImpl::createMysqlUpdateEntityByIdXml;
        final BiFunction<GenerateConfig, List<? extends GenField>, String> createClickHouseInsertIntoXml = GenUpdateByIdImpl::createClickHouseUpdateEntityByIdXml;
        put(DataBaseType.MYSQL, createMysqlIntertIntoXml);
        put(DataBaseType.POSTGRESQL, createPgIntertIntoXml);
        put(DataBaseType.CLICKHOUSE, createClickHouseInsertIntoXml);
    }};


    private static String createUpdateEntityByIdXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        return strategys.get(generateConfig.getDataBaseType()).apply(generateConfig, genFields);
    }


    String updateEntityByIdTpl = " void updateEntityById(@Param(\"m\") {entityName} entity);\n";

    String updateEntityByIdXmlTpl = "\r\n<update id=\"updateEntityById\">\n" +
            "        {content}\n" +
            "        </update>";

    String updateEntityByIdTplServiceTpl = " void updateEntityById({entityName} entity);\n";

    String updateEntityByIdTplServiceImplTpl = "" +
            "    @Override\n" +
            "    public void updateEntityById({entityName} entity) {\n" +
            "        this.baseMapper.updateEntityById(entity);\n" +
            "    }\n";


    @Override
    protected String genCodeByTpl(String tpl) {
        return super.genCodeByTpl(tpl).replace("{content}", createUpdateEntityByIdXml(generateConfig, queryXX));
    }

    @Override
    public String build() {
        switch (genFileType) {
            case mapper_java:
                return this.genCodeByTpl(updateEntityByIdTpl);
            case mapper_xml:
                return this.genCodeByTpl(updateEntityByIdXmlTpl);
            case service_java:
                return super.genCodeByTpl(updateEntityByIdTplServiceTpl);
            case serviceImpl_java:
                return super.genCodeByTpl(updateEntityByIdTplServiceImplTpl);
        }
        return "";
    }

    private static String createMysqlUpdateEntityByIdXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        StringBuilder sql = new StringBuilder(String.format("update %s set ", generateConfig.getTableName()));
        StringJoiner updateJoiner = new StringJoiner(",");
        StringJoiner whereJoiner = new StringJoiner(" ");

        genFields.forEach(c -> {
            if (!c.getIsPrimaryKey()) {
                updateJoiner.add(c.getColumnName() + " = " + "#{m." + c.getPropertyName() + "}");
            } else {
                whereJoiner.add(c.getColumnName() + " = " + "#{m." + c.getPropertyName() + "}");
            }
        });

        sql.append(updateJoiner).append(whereJoiner);

        return sql.toString();
    }

    private static String createClickHouseUpdateEntityByIdXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        StringBuilder sql = new StringBuilder(String.format("alter table %s update ", generateConfig.getTableName()));
        StringJoiner updateJoiner = new StringJoiner(",");
        StringJoiner whereJoiner = new StringJoiner(" ");

        genFields.forEach(c -> {
            if (!c.getIsPrimaryKey()) {
                updateJoiner.add(TemplateUtils.genChMapperXmlCondition(c));
            } else {
                whereJoiner.add(TemplateUtils.genChMapperXmlCondition(c));
            }
        });

        sql.append(updateJoiner).append(whereJoiner);

        return sql.toString();
    }
}

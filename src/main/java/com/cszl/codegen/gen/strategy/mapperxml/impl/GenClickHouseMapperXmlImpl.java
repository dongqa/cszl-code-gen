package com.cszl.codegen.gen.strategy.mapperxml.impl;

import cn.hutool.core.collection.CollUtil;
import com.cszl.codegen.base.enums.DataBaseType;
import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.GenMapperXmlUtil;
import com.cszl.codegen.gen.strategy.mapperxml.DataBaseTypeAnno;
import com.cszl.codegen.gen.strategy.mapperxml.GenMapperXmlStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

/**
 * @Author: Caiyx
 * @Date: 2022/05/19 13:42
 * @Description:
 */
@Component
@DataBaseTypeAnno(value = DataBaseType.CLICKHOUSE)
public class GenClickHouseMapperXmlImpl implements GenMapperXmlStrategy {

    @Override
    public String createUpdateByXXXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        StringBuilder sql=new StringBuilder();
        sql.append("\r\n");
        sql.append("alter table ");
        sql.append(generateConfig.getTableName());
        sql.append(" update ");
        sql.append(GenMapperXmlUtil.genUpdateFieldSql(generateConfig.getGenFieldList()));
        sql.append(GenMapperXmlUtil.genWhereSql(genFields));
        return sql.toString();
    }

    @Override
    public String createUpdateXXByXXXml(GenerateConfig generateConfig, List<? extends GenField> queryXX,List<? extends GenField> updateXX) {
        if(CollUtil.isEmpty(updateXX)){
            return null;
        }
        StringBuilder sql=new StringBuilder();
        sql.append("\r\n");
        sql.append("alter table ");
        sql.append(generateConfig.getTableName());
        sql.append(" update ");
        sql.append(GenMapperXmlUtil.genUpdateFieldSql(updateXX));
        sql.append(GenMapperXmlUtil.genWhereSql(queryXX));
        return sql.toString();
    }

    @Override
    public String createDeleteByXXXml(GenerateConfig generateConfig, List<? extends GenField> queryXX) {
        StringBuilder sql=new StringBuilder();
        sql.append("\r\n");
        sql.append("alter table ");
        sql.append(generateConfig.getTableName());
        sql.append(" delete ");
        sql.append(GenMapperXmlUtil.genWhereSql(queryXX));
        return sql.toString();
    }

    @Override
    public String createInsertIntoXML(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        StringBuilder sql = new StringBuilder(String.format("insert into %s ( \r\n", generateConfig.getTableName()));
        StringJoiner columnJoiner = new StringJoiner(",\r\n");
        StringJoiner fieldJoiner = new StringJoiner(",\r\n");
        genFields.forEach(c -> {
            columnJoiner.add(c.getColumnName());
            fieldJoiner.add("#{l." + c.getPropertyName() + "}");
        });
        sql.append(columnJoiner);
        sql.append(") \r\n");
        sql.append(" values \r\n");
        sql.append("<foreach collection=\"list\" separator=\",\" item=\"l\"> \r\n");
        sql.append("(");
        sql.append(fieldJoiner);
        sql.append(") \r\n");
        sql.append("</foreach> \r\n");
        return sql.toString();
    }
}

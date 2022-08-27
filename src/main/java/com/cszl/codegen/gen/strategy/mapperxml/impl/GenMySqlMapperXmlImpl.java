package com.cszl.codegen.gen.strategy.mapperxml.impl;

import com.cszl.codegen.base.enums.DataBaseType;
import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.mapperxml.DataBaseTypeAnno;
import com.cszl.codegen.gen.strategy.mapperxml.GenMapperXmlStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

/**
 * @Author: Caiyx
 * @Date: 2022/05/19/10:47
 * @Description: 生成MySql mybatis xml模板
 */
@Component
@DataBaseTypeAnno(value = DataBaseType.MYSQL)
public class GenMySqlMapperXmlImpl implements GenMapperXmlStrategy {
    @Override
    public String createInsertIntoXML(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        StringBuilder sql = new StringBuilder(String.format("insert into %s ( \r\n", generateConfig.getTableName()));
        StringJoiner columnJoiner = new StringJoiner(",\r\n");
        StringJoiner fieldJoiner = new StringJoiner(",\r\n");
        StringJoiner duplicateJoiner = new StringJoiner(",\r\n");
        genFields.forEach(c -> {
            columnJoiner.add(c.getColumnName());
            fieldJoiner.add("#{l." + c.getPropertyName() + "}");
            if (!c.getIsPrimaryKey()) {
                duplicateJoiner.add(c.getColumnName() + " = " + "values(" + c.getColumnName() + ")");
            }
        });
        sql.append(columnJoiner);
        sql.append(") \r\n");
        sql.append(" values \r\n");
        sql.append("<foreach collection=\"list\" separator=\",\" item=\"l\"> \r\n");
        sql.append("(");
        sql.append(fieldJoiner);
        sql.append(") \r\n");
        sql.append("</foreach> \r\n");
        sql.append(" on duplicate key update \r\n");
        sql.append(duplicateJoiner);
        return sql.toString();
    }
}

package com.cszl.codegen.gen.strategy.mapperxml.impl;

import com.cszl.codegen.base.enums.DataBaseType;
import com.cszl.codegen.base.exceptions.CreateSqlException;
import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.mapperxml.DataBaseTypeAnno;
import com.cszl.codegen.gen.strategy.mapperxml.GenMapperXmlStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

/**
 * @Author: Caiyx
 * @Date: 2022/05/19 15:13
 * @Description:
 */
@Component
@DataBaseTypeAnno(value = DataBaseType.POSTGRESQL)
public class GenPostgreSqlMapperXmlImpl implements GenMapperXmlStrategy {
    @Override
    public String createInsertIntoXML(GenerateConfig generateConfig, List<? extends GenField> genFields) {

        final GenField primaryField = genFields.parallelStream().filter(f -> ((GenField) f).getIsPrimaryKey()).findFirst().orElseThrow(() -> new CreateSqlException("table " + generateConfig.getTableName() + " primary key does not exist "));

        StringBuilder sql = new StringBuilder(String.format("insert into %s ( \r\n", generateConfig.getTableName()));
        StringJoiner columnJoiner = new StringJoiner(",\r\n");
        StringJoiner fieldJoiner = new StringJoiner(",\r\n");
        StringJoiner duplicateJoiner = new StringJoiner(",\r\n");
        genFields.forEach(c -> {
            columnJoiner.add(c.getColumnName());
            // jsonb特殊处理
            String prefix = "#{l." + c.getPropertyName() + "}";
            // jsonb json特殊处理
            String suffix = "jsonb".equals(c.getColumnType()) ? "::jsonb" : "json".equals(c.getColumnType()) ? "::json" : "";
            fieldJoiner.add(prefix + suffix);

            if (!c.getIsPrimaryKey()) {
                duplicateJoiner.add(c.getColumnName() + " = " + "EXCLUDED." + c.getColumnName());
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
        sql.append(String.format(" on conflict (%s) do update set \r\n", primaryField.getColumnName()));
        sql.append(duplicateJoiner);
        return sql.toString();
    }
}

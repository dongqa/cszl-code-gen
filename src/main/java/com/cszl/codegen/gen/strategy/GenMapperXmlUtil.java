package com.cszl.codegen.gen.strategy;

import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenQueryField;
import com.cszl.codegen.gen.config.GenerateConfig;

import java.util.List;

/**
 * @Author: Caiyx
 * @Date: 2022/05/19 14:59
 * @Description:
 */
public class GenMapperXmlUtil {

    /**
     * 生成where条件语句
     * @param genFields
     * @return
     */
    public static String genWhereSql(List<? extends GenField> genFields){
        StringBuilder sql=new StringBuilder();
        sql.append("\r\n");
        sql.append("<where>");
        sql.append("\r\n");
        genFields.forEach(c->{
            GenQueryField queryField = (GenQueryField) c;
            sql.append(String.format("<if test=\"%s!=null",c.getPropertyName()));
            if(c.getPropertyType().equalsIgnoreCase("string")){
                sql.append(String.format(" and %s!=''",c.getPropertyName()));
            }
            sql.append("\">");
            sql.append("\r\n");
            sql.append(((GenQueryField) c).getQueryConditionRel().d);
            sql.append(String.format(" %s ",c.getColumnName()));
            sql.append(String.format(queryField.getQueryRel().defaultDialectMybatisExpress,c.getPropertyName()));
            sql.append("\r\n");
            sql.append("</if>");
            sql.append("\r\n");
        });
        sql.append("\r\n");
        sql.append("</where>");
        sql.append("\r\n");
        return sql.toString();
    }

    /**
     * 生成update更新字段语句
     * @param updateFieldList
     * @return
     */
    public static String genUpdateFieldSql(List<? extends GenField> updateFieldList) {
        StringBuilder sql=new StringBuilder();
        sql.append("\r\n");
        sql.append("<trim suffixOverrides=\",\">");
        sql.append("\r\n");
        updateFieldList.forEach(c->{
            sql.append(String.format("<if test=\"%s!=null and %s!=''\">",c.getPropertyName(),c.getPropertyName()));
            sql.append(c.getColumnName());
            sql.append("=");
            sql.append(String.format("#{%s}",c.getPropertyName()));
            sql.append(",");
            sql.append("</if>");
            sql.append("\r\n");
        });
        sql.append("</trim>");
        sql.append("\r\n");
        return sql.toString();
    }
}

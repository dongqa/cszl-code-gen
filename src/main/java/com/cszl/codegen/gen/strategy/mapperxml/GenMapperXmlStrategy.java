package com.cszl.codegen.gen.strategy.mapperxml;

import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.GenMapperXmlUtil;

import java.util.List;

public interface GenMapperXmlStrategy {
    default String createFindOneByXXXml(GenerateConfig generateConfig, List<? extends GenField> genFields){
        StringBuilder sql=new StringBuilder();
        sql.append("\r\n");
        sql.append("select * from ");
        sql.append(generateConfig.getTableName());
        sql.append(GenMapperXmlUtil.genWhereSql(genFields));
        return sql.toString();
    }

    default String createUpdateByXXXml(GenerateConfig generateConfig, List<? extends GenField> genFields){
        StringBuilder sql=new StringBuilder();
        sql.append("\r\n");
        sql.append("update ");
        sql.append(generateConfig.getTableName());
        sql.append(" set ");
        sql.append(GenMapperXmlUtil.genUpdateFieldSql(generateConfig.getGenFieldList()));
        sql.append(GenMapperXmlUtil.genWhereSql(genFields));
        return sql.toString();
    }

    default String createListByXXXml(GenerateConfig generateConfig, List<? extends GenField> genFields){
        return createFindOneByXXXml(generateConfig,genFields);
    }

    default String createPageByXXXml(GenerateConfig generateConfig, List<? extends GenField> genFields){
        return createFindOneByXXXml(generateConfig,genFields);
    }

    default String createUpdateXXByXXXml(GenerateConfig generateConfig, List<? extends GenField> queryXX,List<? extends GenField> updateXX){
        StringBuilder sql=new StringBuilder();
        sql.append("update ");
        sql.append(generateConfig.getTableName());
        sql.append(" set ");
        sql.append(GenMapperXmlUtil.genUpdateFieldSql(updateXX));
        sql.append(GenMapperXmlUtil.genWhereSql(queryXX));
        return sql.toString();
    }

    default String createDeleteByXXXml(GenerateConfig generateConfig, List<? extends GenField> queryXX){
        StringBuilder sql=new StringBuilder();
        sql.append("delete from ");
        sql.append(generateConfig.getTableName());
        sql.append(GenMapperXmlUtil.genWhereSql(queryXX));
        return sql.toString();
    }

    String createInsertIntoXML(GenerateConfig generateConfig, List<? extends GenField> genFields);
}

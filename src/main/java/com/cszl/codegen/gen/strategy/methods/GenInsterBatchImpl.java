package com.cszl.codegen.gen.strategy.methods;

import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.GenMethodStrategy;
import com.cszl.codegen.gen.strategy.mapperxml.GenMapperXmlContext;

import java.util.List;

/**
 * 构建InsertBatch
 * create by wdq on 2021/4/12 10:03
 */
public class GenInsterBatchImpl extends GenMethodStrategy {

    String insertBatchTpl = " void insertBatch(@Param(\"list\") List<{entityName}> list);\n";

    String insertBatchXmlTpl = "\r\n<update id=\"insertBatch\">\n" +
            "        {content}\n" +
            "        </update>";

    String insertBatchTplServiceTpl = " void insertBatch(List<{entityName}> list);\n";

    String insertBatchTplServiceImplTpl = "" +
            "    @Override\n" +
            "    public void insertBatch(List<{entityName}> list) {\n" +
            "        this.baseMapper.insertBatch(list);\n" +
            "    }\n";


    @Override
    public String build() {
        switch (genFileType) {
            case mapper_java:
                return this.genCodeByTpl(insertBatchTpl);
            case mapper_xml:
                return this.genCodeByTpl(insertBatchXmlTpl);
            case service_java:
                return super.genCodeByTpl(insertBatchTplServiceTpl);
            case serviceImpl_java:
                return super.genCodeByTpl(insertBatchTplServiceImplTpl);
        }
        return "";
    }

    @Override
    protected String genCodeByTpl(String tpl) {
        return tpl.replace("{entityName}", generateConfig.getEntityName())
                .replace("{content}", createIntertIntoXml(generateConfig, queryXX));
    }

    private static String createIntertIntoXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        return new GenMapperXmlContext(generateConfig.getDataBaseType()).createInsertBatchXml(generateConfig,genFields);
    }
}

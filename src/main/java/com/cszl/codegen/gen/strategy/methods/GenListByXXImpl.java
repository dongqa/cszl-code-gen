package com.cszl.codegen.gen.strategy.methods;

import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.GenMethodStrategy;
import com.cszl.codegen.gen.strategy.mapperxml.GenMapperXmlContext;

import java.util.List;

/**
 * 构建listByXX
 * create by wdq on 2021/4/12 10:03
 */
public class GenListByXXImpl extends GenMethodStrategy {

    String listByXXControllerTpl = "" +
            "    @PostMapping(value = \"listBy{byParams}\")\n" +
            "    @ResponseBody\n" +
            "    @ApiOperation(value = \"根据【{byParamsWithSeparator}】查询{tableComment}列表\")\n" +
            "    public Result listBy{byParams}({requestName} params){\n" +
            "        return Result.ok().data((Serializable)service.listBy{byParams}(params));\n" +
            "    }\n";

    String listByXXServiceTpl = " List<{entityName}> listBy{byParams}({requestName} params);\n";

    String listByXXServiceImplTpl = "" +
            "    @Override\n" +
            "    public List<{entityName}> listBy{byParams}({requestName} params) {\n" +
            "        return this.list(new LambdaQueryWrapper<{entityName}>(){lambdaQueryParams});\n" +
            "    }\n";

    String listByXXXmlTpl ="\r\n"+
            "<select id=\"findOneBy{byParams}\" resultMap=\"BaseResultMap\">"+
            "{content}\n"+
            "</select>";

    @Override
    public String build() {
        switch (genFileType) {
            case service_java:
                return super.genCodeByTpl(listByXXServiceTpl);
            case serviceImpl_java:
                return super.genCodeByTpl(listByXXServiceImplTpl);
            case controller_java:
                return super.genCodeByTpl(listByXXControllerTpl);
            case mapper_xml:
                return this.genCodeByTpl(listByXXXmlTpl);
        }
        return "";
    }

    @Override
    protected String genCodeByTpl(String tpl) {
        return super.genCodeByTpl(tpl)
                .replace("{content}", createSelectXml(generateConfig, queryXX));
    }

    private String createSelectXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        return new GenMapperXmlContext(generateConfig.getDataBaseType()).createListByXXXml(generateConfig,genFields);
    }


}

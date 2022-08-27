package com.cszl.codegen.gen.strategy.methods;

import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.GenMethodStrategy;
import com.cszl.codegen.gen.strategy.mapperxml.GenMapperXmlContext;

import java.util.List;

/**
 * 构建pageByXX
 * create by wdq on 2021/4/12 10:03
 */
public class GenPageByXXImpl extends GenMethodStrategy {

    String pageByXXControllerTpl = "" +
            "    @PostMapping(value = \"pageBy{byParams}\")\n" +
            "    @ResponseBody\n" +
            "    @ApiOperation(value = \"根据【{byParamsWithSeparator}】分页查询{tableComment}数据\")\n" +
            "    public Result pageBy{byParams}({requestName} params, Page page){\n" +
            "        return Result.ok().data((Serializable)service.pageBy{byParams}(params, page));\n" +
            "    }\n";

    String pageByXXServiceTpl = " Page<{entityName}> pageBy{byParams}({requestName} params, Page page);\n";

    String pageByXXServiceImplTpl = "" +
            "    @Override\n" +
            "    public Page<{entityName}> pageBy{byParams}({requestName} params, Page page) {\n" +
            "        return (Page)this.page(page, new LambdaQueryWrapper<{entityName}>(){lambdaQueryParams});\n" +
            "    }\n";

    String pageByXXXmlTpl ="\r\n"+
            "<select id=\"findOneBy{byParams}\" resultMap=\"BaseResultMap\">"+
            "{content}\n"+
            "</select>";

    @Override
    public String build() {
        switch (genFileType) {
            case service_java:
                return super.genCodeByTpl(pageByXXServiceTpl);
            case serviceImpl_java:
                return super.genCodeByTpl(pageByXXServiceImplTpl);
            case controller_java:
                return super.genCodeByTpl(pageByXXControllerTpl);
            case mapper_xml:
                return this.genCodeByTpl(pageByXXXmlTpl);
        }
        return "";
    }

    @Override
    protected String genCodeByTpl(String tpl) {
        return super.genCodeByTpl(tpl)
                .replace("{content}", createSelectXml(generateConfig, queryXX));
    }

    private String createSelectXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        return new GenMapperXmlContext(generateConfig.getDataBaseType()).createPageByXXXml(generateConfig,genFields);
    }
}

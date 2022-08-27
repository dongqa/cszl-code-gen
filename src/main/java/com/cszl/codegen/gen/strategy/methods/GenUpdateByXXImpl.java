package com.cszl.codegen.gen.strategy.methods;

import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.GenMethodStrategy;
import com.cszl.codegen.gen.strategy.mapperxml.GenMapperXmlContext;

import java.util.List;

/**
 * 构建updateByXX
 * create by wdq on 2021/4/12 10:03
 */
public class GenUpdateByXXImpl extends GenMethodStrategy {

    String updateByXXControllerTpl = "" +
            "    @PostMapping(value = \"updateBy{byParams}\")\n" +
            "    @ResponseBody\n" +
            "    @ApiOperation(value = \"根据【{byParamsWithSeparator}】查询更新{tableComment}\")\n" +
            "    public Result updateBy{byParams}({requestName} params){\n" +
            "        service.updateBy{byParams}(params,null);\n" +
            "        return Result.ok();\n" +
            "    }\n";

    String updateByXXServiceTpl = "void updateBy{byParams}({requestName} params ,{entityName} entity);\n";

    String updateByXXServiceImplTpl = "" +
            "    @Override\n" +
            "    public void updateBy{byParams}({requestName} params ,{entityName} entity) {\n" +
            "        this.update(entity,new LambdaQueryWrapper<{entityName}>(){lambdaQueryParams});\n" +
            "    }\n";

    String updateByXXXmlTpl="\r\n"+
            "<update id=\"updateBy{byParams}\">"+
            "{content}\n"+
            "</update>";

    @Override
    public String build() {
        switch (genFileType) {
            case service_java:
                return super.genCodeByTpl(updateByXXServiceTpl);
            case serviceImpl_java:
                return super.genCodeByTpl(updateByXXServiceImplTpl);
            case controller_java:
                return super.genCodeByTpl(updateByXXControllerTpl);
            case mapper_xml:
                return this.genCodeByTpl(updateByXXXmlTpl);
        }
        return "";
    }

    @Override
    protected String genCodeByTpl(String tpl) {
        return super.genCodeByTpl(tpl)
                .replace("{content}", createUpdateXml(generateConfig, queryXX));
    }

    private String createUpdateXml(GenerateConfig generateConfig, List<? extends GenField> genFields){
        return new GenMapperXmlContext(generateConfig.getDataBaseType()).createUpdateByXXXml(generateConfig,genFields);
    }
}

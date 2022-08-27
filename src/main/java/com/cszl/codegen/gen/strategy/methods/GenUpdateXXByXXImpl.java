package com.cszl.codegen.gen.strategy.methods;

import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.GenMethodStrategy;
import com.cszl.codegen.gen.strategy.mapperxml.GenMapperXmlContext;

import java.util.List;

/**
 * 构建updateXXByXX
 * create by wdq on 2021/4/12 10:03
 */
public class GenUpdateXXByXXImpl extends GenMethodStrategy {

    private final static String updateXXByXXControllerTpl = "" +
            "    @PostMapping(value = \"update{updateParams}By{byParams}\")\n" +
            "    @ResponseBody\n" +
            "    @ApiOperation(value = \"根据【{byParamsWithSeparator}】查询更新{tableComment}中的【{updateParamsWithSeparator}】字段\")\n" +
            "    public Result update{updateParams}By{byParams}({requestName} params){\n" +
            "        service.update{updateParams}By{byParams}(params,null);\n" +
            "        return Result.ok();\n" +
            "    }\n";

    private final static String updateXXByXXServiceTpl = "void update{updateParams}By{byParams}({requestName} params, {entityName} entity);\n";

    private final static String updateXXByXXServiceImplTpl = "" +
            "    @Override\n" +
            "    public void update{updateParams}By{byParams}({requestName} params, {entityName} entity) {\n" +
            "        this.update(new LambdaUpdateWrapper<{entityName}>(){lambdaQueryParams}{lambdaUpdateParams});\n" +
            "    }\n";

    String updateXXByXXXmlTpl="\r\n"+
            "<update id=\"updateBy{byParams}\">"+
            "{content}\n"+
            "</update>";

    @Override
    public String build() {
        switch (genFileType) {
            case service_java:
                return super.genCodeByTpl(updateXXByXXServiceTpl);
            case serviceImpl_java:
                return super.genCodeByTpl(updateXXByXXServiceImplTpl);
            case controller_java:
                return super.genCodeByTpl(updateXXByXXControllerTpl);
            case mapper_xml:
                return this.genCodeByTpl(updateXXByXXXmlTpl);
        }
        return "";
    }

    @Override
    protected String genCodeByTpl(String tpl) {
        return super.genCodeByTpl(tpl)
                .replace("{content}", createUpdateXml(generateConfig, queryXX,updateXX));
    }

    private String createUpdateXml(GenerateConfig generateConfig, List<? extends GenField> queryXX,List<? extends GenField> updateXX){
        return new GenMapperXmlContext(generateConfig.getDataBaseType()).createUpdateXXByXXXml(generateConfig,queryXX,updateXX);
    }

}

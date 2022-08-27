package com.cszl.codegen.gen.strategy.methods;

import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.GenMethodStrategy;
import com.cszl.codegen.gen.strategy.mapperxml.GenMapperXmlContext;

import java.util.List;

/**
 * 构建DeleteByXX
 * create by wdq on 2021/4/12 10:03
 *
 * @author wdq
 */
public class GenDeleteByXXImpl extends GenMethodStrategy {

    String deleteByXXControllerTpl = "" +
            "    @PostMapping(value = \"deleteBy{byParams}\")\n" +
            "    @ResponseBody\n" +
            "    @ApiOperation(value = \"根据【{byParamsWithSeparator}】删除{tableComment}\")\n" +
            "    public Result deleteBy{byParams}({requestName} params){\n" +
            "        service.deleteBy{byParams}(params);\n" +
            "        return Result.ok();\n" +
            "    }\n";

    String deleteByXXServiceTpl = "void deleteBy{byParams}({requestName} params);\n";

    String deleteByXXServiceImplTpl = "" +
            "    @Override\n" +
            "    public void deleteBy{byParams}({requestName} params) {\n" +
            "        this.remove(new LambdaQueryWrapper<{entityName}>(){lambdaQueryParams});\n" +
            "    }\n";

    String deleteByXXXmlTpl="\r\n"+
            "<delete id=\"deleteBy{byParams}\">"+
            "{content}\n"+
            "</delete>";

    @Override
    public String build() {
        switch (genFileType) {
            case service_java:
                return super.genCodeByTpl(deleteByXXServiceTpl);
            case serviceImpl_java:
                return super.genCodeByTpl(deleteByXXServiceImplTpl);
            case controller_java:
                return super.genCodeByTpl(deleteByXXControllerTpl);
            case mapper_xml:
                return this.genCodeByTpl(deleteByXXXmlTpl);
            default:
                return "";
        }
    }

    @Override
    protected String genCodeByTpl(String tpl) {
        return super.genCodeByTpl(tpl)
                .replace("{content}", createDeleteXml(generateConfig, queryXX));
    }

    private String createDeleteXml(GenerateConfig generateConfig, List<? extends GenField> queryXX){
        return new GenMapperXmlContext(generateConfig.getDataBaseType()).createDeleteByXXXml(generateConfig,queryXX);
    }

}

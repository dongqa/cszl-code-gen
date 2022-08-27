package com.cszl.codegen.gen.strategy.methods;

import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.strategy.GenMethodStrategy;
import com.cszl.codegen.gen.strategy.mapperxml.GenMapperXmlContext;

import java.util.List;

/**
 * 构建FindOneByXX
 * create by wdq on 2021/4/12 10:03
 */
public class GenFindOneByXXImpl extends GenMethodStrategy {
//    private final static Map<DataBaseType, BiFunction<GenerateConfig, List<? extends GenField>, String>> strategys = new HashMap<DataBaseType, BiFunction<GenerateConfig, List<? extends GenField>, String>>() {{
//        final BiFunction<GenerateConfig, List<? extends GenField>, String> createMysqlSelectXml = GenFindOneByXXImpl::createMysqlFindOneXml;
////        final BiFunction<GenerateConfig, List<? extends GenField>, String> createPgSelectXml = GenFindOneByXXImpl::createPgSelectXml;
//
//        put(DataBaseType.MYSQL, createMysqlSelectXml);
////        put(DataBaseType.POSTGRESQL, createPgSelectXml);
////        put(DataBaseType.CLICKHOUSE, GenFindOneByXXImpl::createClickHouseSelectXml);
//    }};

    String findOneByXXControllerTpl = "" +
            "    @GetMapping(value = \"findOneBy{byParams}\")\n" +
            "    @ResponseBody\n" +
            "    @ApiOperation(value = \"根据【{byParamsWithSeparator}】获取单个{tableComment}\")\n" +
            "    public Result findOneBy{byParams}({requestName} params){\n" +
            "       {entityName} data = service.findOneBy{byParams}(params);\n" +
            "        return Result.ok().data(data);\n" +
            "    }\n";

    String findOneByXXServiceTpl = "{entityName} findOneBy{byParams}({requestName} params);\n";

    String findOneByXXServiceImplTpl = "" +
            "    @Override\n" +
            "    public {entityName} findOneBy{byParams}({requestName} params) {\n" +
            "        return this.getOne(new LambdaQueryWrapper<{entityName}>(){lambdaQueryParams});\n" +
            "    }\n";

    String findOneByXXXmlTpl="\r\n"+
            "<select id=\"findOneBy{byParams}\" resultMap=\"BaseResultMap\">"+
            "{content}\n"+
            "</select>";

    @Override
    public String build() {
        switch (genFileType) {
            case service_java:
                return super.genCodeByTpl(findOneByXXServiceTpl);
            case serviceImpl_java:
                return super.genCodeByTpl(findOneByXXServiceImplTpl);
            case controller_java:
                return super.genCodeByTpl(findOneByXXControllerTpl);
            case mapper_xml:
                return this.genCodeByTpl(findOneByXXXmlTpl);
            default:
                return "";
        }
    }

    @Override
    protected String genCodeByTpl(String tpl) {
        return super.genCodeByTpl(tpl)
                .replace("{content}", createSelectXml(generateConfig, queryXX));
    }

    private String createSelectXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        return new GenMapperXmlContext(generateConfig.getDataBaseType()).createFindOneByXXXml(generateConfig,genFields);
    }

}

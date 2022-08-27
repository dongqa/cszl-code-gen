package com.cszl.codegen.gen.strategy.mapperxml;

import cn.hutool.extra.spring.SpringUtil;
import com.cszl.codegen.base.enums.DataBaseType;
import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenerateConfig;

import java.util.List;

/**
 * @Author: Caiyx
 * @Date: 2022/05/19 10:59
 * @Description:
 */
public class GenMapperXmlContext {
    private GenMapperXmlStrategy genMapperXmlStrategy;

    public GenMapperXmlContext(DataBaseType dataBaseType){
        GenMapperXmlStrategyFactory genMapperXmlStrategyFactory = SpringUtil.getBean("genMapperXmlStrategyFactory",GenMapperXmlStrategyFactory.class);
        genMapperXmlStrategy=genMapperXmlStrategyFactory.getGenMapperXmlStrategy(dataBaseType);
    }

    public String createFindOneByXXXml(GenerateConfig generateConfig, List<? extends GenField> genFields){
        return genMapperXmlStrategy.createFindOneByXXXml(generateConfig,genFields);
    }

    public String createUpdateByXXXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        return genMapperXmlStrategy.createUpdateByXXXml(generateConfig,genFields);
    }

    public String createListByXXXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        return genMapperXmlStrategy.createListByXXXml(generateConfig,genFields);
    }

    public String createPageByXXXml(GenerateConfig generateConfig, List<? extends GenField> genFields){
        return genMapperXmlStrategy.createPageByXXXml(generateConfig,genFields);
    }

    public String createUpdateXXByXXXml(GenerateConfig generateConfig, List<? extends GenField> queryXX,List<? extends GenField> updateXX) {
        return genMapperXmlStrategy.createUpdateXXByXXXml(generateConfig,queryXX,updateXX);
    }

    public String createDeleteByXXXml(GenerateConfig generateConfig, List<? extends GenField> queryXX) {
        return genMapperXmlStrategy.createDeleteByXXXml(generateConfig,queryXX);
    }

    public String createInsertBatchXml(GenerateConfig generateConfig, List<? extends GenField> genFields) {
        return genMapperXmlStrategy.createInsertIntoXML(generateConfig,genFields);
    }
}

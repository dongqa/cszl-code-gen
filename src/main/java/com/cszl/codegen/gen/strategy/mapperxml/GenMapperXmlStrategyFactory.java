package com.cszl.codegen.gen.strategy.mapperxml;

import cn.hutool.core.collection.CollUtil;
import com.cszl.codegen.base.enums.DataBaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Caiyx
 * @Date: 2022/05/19 10:50
 * @Description:
 */
@Component
public class GenMapperXmlStrategyFactory {
    @Lazy
    @Autowired
    private List<GenMapperXmlStrategy> genMapperXmlStrategyList;

    public GenMapperXmlStrategy getGenMapperXmlStrategy(DataBaseType dataBaseType) {
        if(CollUtil.isEmpty(genMapperXmlStrategyList)){
            return null;
        }
        GenMapperXmlStrategy nvrConfigStrategy = genMapperXmlStrategyList.stream().filter(m -> m.getClass().getAnnotation(DataBaseTypeAnno.class).value().equals(dataBaseType)).findAny().orElse(null);
        return nvrConfigStrategy;
    }
}

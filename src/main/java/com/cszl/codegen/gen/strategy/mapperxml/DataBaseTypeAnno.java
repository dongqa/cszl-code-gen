package com.cszl.codegen.gen.strategy.mapperxml;

import com.cszl.codegen.base.enums.DataBaseType;

import java.lang.annotation.*;

/**
 * @Author: Caiyx
 * @Date: 2022/05/19/10:47
 * @Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataBaseTypeAnno {
    DataBaseType value();
}

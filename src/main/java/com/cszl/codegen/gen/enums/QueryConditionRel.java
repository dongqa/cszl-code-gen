package com.cszl.codegen.gen.enums;

import com.cszl.codegen.base.utils.StringUtils;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * 查询条件关系
 * create by wdq on 2021/6/26 12:28
 */
public enum QueryConditionRel {

    AND("and", StringUtils::concatByAnd), OR("or", StringUtils::concatByOR);

    public String d;

    public BinaryOperator<String> concat;

    QueryConditionRel(String d, BinaryOperator<String> concat) {
        this.d = d;
        this.concat = concat;
    }
}

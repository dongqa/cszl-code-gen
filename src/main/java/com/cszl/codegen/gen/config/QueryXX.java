package com.cszl.codegen.gen.config;

import com.cszl.codegen.gen.enums.QueryConditionRel;
import com.cszl.codegen.gen.enums.QueryRel;
import lombok.Data;

/**
 * 查询参数
 * create by wdq on 2021/6/26 12:14
 */
@Data
public class QueryXX {

    /**
     * 字段名称
     */
    private String name;

    /**
     * 查询关系
     */
    private QueryRel queryRel;

    /**
     * 查询条件关系
     */
    private QueryConditionRel queryConditionRel;

}

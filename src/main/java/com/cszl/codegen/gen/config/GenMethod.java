package com.cszl.codegen.gen.config;

import com.cszl.codegen.gen.enums.GenMethodName;
import com.cszl.codegen.gen.enums.QueryRel;
import lombok.Data;

import java.util.List;

/**
 * 生成的方法
 * create by wdq on 2021/4/9 17:49
 */
@Data
public class GenMethod {

    /**
     * 方法类型
     */
    private GenMethodName genMethodName;

    /**
     * 更新的参数
     */
    private List<String> updateXX;

    /**
     * 查询的参数
     */
    private List<QueryXX> queryXX;

    /**
     * 查询的参数条件关系
     */
    private QueryRel queryRel;

}

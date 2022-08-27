package com.cszl.codegen.gen.config;

import com.cszl.codegen.gen.enums.QueryConditionRel;
import com.cszl.codegen.gen.enums.QueryRel;
import lombok.Data;

/**
 * 查询字段
 * create by wdq on 2021/6/26 12:43
 */
@Data
public class GenQueryField extends GenField {

    private QueryRel queryRel;

    private QueryConditionRel queryConditionRel;

}

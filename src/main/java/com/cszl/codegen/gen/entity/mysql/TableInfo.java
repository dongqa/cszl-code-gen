package com.cszl.codegen.gen.entity.mysql;

import com.cszl.codegen.base.entity.BaseInfo;
import lombok.Data;

/**
 * mysql表信息
 * create by wdq on 2021/4/9 12:56
 */
@Data
public class TableInfo extends BaseInfo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

}

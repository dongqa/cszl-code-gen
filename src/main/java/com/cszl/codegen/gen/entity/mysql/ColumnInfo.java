package com.cszl.codegen.gen.entity.mysql;

import com.cszl.codegen.base.entity.BaseInfo;
import lombok.Data;

/**
 * mysql 字段对象
 * create by wdq on 2021/4/9 13:26
 */
@Data
public class ColumnInfo extends BaseInfo {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 是否可以为空
     */
    private String isNullable;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 字段键
     */
    private String columnKey;

    /**
     * 扩展信息
     */
    private String extra;

    /**
     * 字段注释
     */
    private String columnComment;
}

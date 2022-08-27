package com.cszl.codegen.gen.config;

import lombok.Data;

/**
 * 创建的字段
 * create by wdq on 2021/4/9 17:34
 */
@Data
public class GenField {

    /**
     * 是否主键
     */
    private Boolean isPrimaryKey;

    /**
     * id类型
     */
    private String idType = "";
    /**
     * 属性名
     */
    private String propertyName;

    /**
     * 属性类型
     */
    private String propertyType;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段名
     */
    private String columnType;

    /**
     * 字段注释
     */
    private String columnComment;

}

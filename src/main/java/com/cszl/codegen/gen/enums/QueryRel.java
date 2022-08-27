package com.cszl.codegen.gen.enums;

/**
 * 查询关系
 * create by wdq on 2021/6/26 12:11
 */
public enum QueryRel {

    EQ("eq","= #{%s}"),
    LE("le","<![CDATA[<=]]> #{%s}"),
    GE("ge",">= #{%s}"),
    LT("lt","<![CDATA[<]]> #{%s}"),
    GT("gt","> #{%s}"),
    LIKE("like","like \"%%\"#{%s}\"%%\""),
    NOT_LIKE("notLike","not like \"%%\"#{%s}\"%%\""),
    LIKE_LEFT("likeLeft","like \"%%\"#{%s}"),
    LIKE_RIGHT("likeRight","like #{%s}\"%%\"");

    public String d;

    public String defaultDialectMybatisExpress;

    QueryRel(String d) {
        this.d = d;
    }

    QueryRel(String d,String defaultDialectMybatisExpress) {
        this.d = d;
        this.defaultDialectMybatisExpress=defaultDialectMybatisExpress;
    }
}

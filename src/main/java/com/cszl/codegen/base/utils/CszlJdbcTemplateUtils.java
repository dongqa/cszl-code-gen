package com.cszl.codegen.base.utils;

import com.cszl.codegen.base.db.DbTemplateFactory;

/**
 * create by wdq on 2021/4/9 16:53
 */
public class CszlJdbcTemplateUtils {

    private final static ThreadLocal<DbTemplateFactory.CszlJdbcTemplate> threadLocal = new ThreadLocal<>();

    public static DbTemplateFactory.CszlJdbcTemplate get() {
        return threadLocal.get();
    }

    public static void setCszlJdbcTemplate(DbTemplateFactory.CszlJdbcTemplate jdbcTemplate) {
        threadLocal.set(jdbcTemplate);
    }

    public static void remove() {
        threadLocal.remove();
    }

}

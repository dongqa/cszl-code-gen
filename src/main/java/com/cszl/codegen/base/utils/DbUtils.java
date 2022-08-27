package com.cszl.codegen.base.utils;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cszl.codegen.base.enums.DataBaseType;
import com.cszl.codegen.base.exceptions.UnsupportDataBaseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * db工具
 * create by wdq on 2021/4/9 16:11
 */
public class DbUtils {

    private final static Pattern pattern = Pattern.compile("(?<=/)([a-zA-Z-0-9_]+)(?=\\?*)");

    /**
     * 解析数据库名称
     * @param url
     * @return
     */
    public static String parseDbName(String url) {
        Assert.notEmpty(url, "url must be not null or empty");
        url = url.replace("//", "");
        Matcher matcher = pattern.matcher(url);
        while (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    public static DataBaseType dbUrlParse(String url) {
        if (url.contains(":mysql:")) {
            return DataBaseType.MYSQL;
        } else if (url.contains(":postgresql:")) {
            return DataBaseType.POSTGRESQL;
        } else if (url.contains(":clickhouse:")) {
            return DataBaseType.CLICKHOUSE;
        } else if (url.contains(":ch:")) {
            return DataBaseType.CLICKHOUSE;
        }
        throw new UnsupportDataBaseException("不支持的数据库类型");
    }
}

package com.cszl.codegen.base.enums;

/**
 * create by wdq on 2021/4/9 12:36
 * @author wdq
 */
public enum DataBaseType {

    /** mysql */
    MYSQL("msql", "com.mysql.cj.jdbc.Driver", "mysql数据库"),
    POSTGRESQL("postgresql", "org.postgresql.Driver", "postgresql数据库"),
    CLICKHOUSE("clickhouse", "com.clickhouse.jdbc.ClickHouseDriver", "clickhouse数据库");
    public String name;
    public String driverClass;
    public String comments;

    DataBaseType(String name, String driverClass, String comments) {
        this.name = name;
        this.driverClass = driverClass;
        this.comments = comments;
    }
}

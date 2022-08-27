package com.cszl.codegen.base.constants;

import com.cszl.codegen.base.enums.DataBaseType;
import com.cszl.codegen.base.enums.DbOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * 元数据查询sql
 * create by wdq on 2021/4/9 15:48
 */
public class MetaQuerySql {

    public static final Map<DataBaseType, Map<DbOperation, String>> USE_SQL = new HashMap<>();

    static {
        USE_SQL.put(DataBaseType.MYSQL, new HashMap<DbOperation, String>() {
            {
                put(DbOperation.queryTable, "select * from information_schema.`tables` where table_schema = ? ");
                put(DbOperation.queryColumn, "select * from information_schema.`columns` where table_schema = ? and table_name = ? ");
            }
        });

        USE_SQL.put(DataBaseType.POSTGRESQL, new HashMap<DbOperation, String>() {
            {
                put(DbOperation.queryTable, "select\n" +
                        "\tt2.relname table_name,\n" +
                        "\tobj_description(t2.oid) table_comment\n" +
                        "from\n" +
                        "\tpg_database t1,\n" +
                        "\tpg_class t2\n" +
                        "where\n" +
                        "\tt1.datdba = t2.relowner\n" +
                        "\tand t1.datname = ?\n" +
                        "\tand t2.relkind = 'r'\n");

                put(DbOperation.queryColumn, "select\n" +
                        "\ttt1.*,\n" +
                        "\tcase\n" +
                        "\t\twhen tt2.conrelid is null then ''\n" +
                        "\t\telse 'PRI'\n" +
                        "\tend\n" +
                        "\tcolumn_key\n" +
                        "from\n" +
                        "\t(\n" +
                        "\tselect\n" +
                        "\t\tt2.relname table_name,\n" +
                        "\t\tcol_description(t3.attrelid, t3.attnum) column_comment,\n" +
                        "\t\tt3.attname column_name,\n" +
                        "\t\tt4.typname data_type,\n" +
                        "\t\tt2.oid tableoid,\n" +
                        "\t\tt3.attnum,\n" +
                        "\t\tcase when t3.attnotnull then 'false' else 'true' end is_nullable\n" +
                        "\tfrom\n" +
                        "\t\tpg_database t1,\n" +
                        "\t\tpg_class t2,\n" +
                        "\t\tpg_attribute t3,\n" +
                        "\t\tpg_type t4\n" +
                        "\twhere\n" +
                        "\t\tt1.datdba = t2.relowner\n" +
                        "\t\tand t2.oid = t3.attrelid\n" +
                        "\t\tand t3.atttypid = t4.oid\n" +
                        "\t\tand t1.datname = ? \n" +
                        "\t\tand t2.relname = ? \n" +
                        "\t\tand t2.relkind = 'r'\n" +
                        "\t\tand t3.attstattarget != 0 ) tt1\n" +
                        "left join pg_constraint tt2 on\n" +
                        "\ttt1.tableoid = tt2.conrelid\n" +
                        "\tand tt2.conkey[1] = tt1.attnum\n");
            }
        });

        USE_SQL.put(DataBaseType.CLICKHOUSE, new HashMap<DbOperation, String>() {
            {
                put(DbOperation.queryTable, "select * from information_schema.`tables` where table_schema = ? ");
                put(DbOperation.queryColumn, "select * from information_schema.`columns` where table_schema = ? and table_name = ? ");
            }
        });
    }
}


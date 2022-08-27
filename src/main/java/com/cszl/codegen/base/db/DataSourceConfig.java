package com.cszl.codegen.base.db;

import com.cszl.codegen.base.enums.DataBaseType;
import com.cszl.codegen.base.exceptions.UnsupportDataBaseException;
import com.cszl.codegen.base.utils.DbUtils;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 数据库配置
 * create by wdq on 2021/4/9 11:07
 */
@Data
public class DataSourceConfig {

    private String url;
    private String username;
    private String password;
    private String driverClass;
    //数据库类型
    private DataBaseType dataBaseType;
    /**
     * 数据库名
     */
    private String dbName;

    public DataSourceConfig() {
    }

    public DataSourceConfig(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getDriverClass() {
        this.dbName = DbUtils.parseDbName(url);
        this.dataBaseType = DbUtils.dbUrlParse(url);
        if (!StringUtils.isEmpty(driverClass)) {
            return driverClass;
        }
        return this.dataBaseType.driverClass;
    }

}

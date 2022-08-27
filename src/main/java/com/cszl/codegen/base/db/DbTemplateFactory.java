package com.cszl.codegen.base.db;

import com.cszl.codegen.base.enums.NamingStrategy;
import com.cszl.codegen.base.exceptions.QueryException;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.AbstractDriverBasedDataSource;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;
import java.util.Properties;

/**
 * jdbctemplate工厂
 * create by wdq on 2021/4/9 11:07
 */
@Slf4j
public class DbTemplateFactory {

    public static CszlJdbcTemplate getInstance(DataSourceConfig dataSourceConfig) {
        CszlJdbcTemplate jdbcTemplate = new CszlJdbcTemplate(dataSourceConfig);
        return jdbcTemplate;
    }


    public static class CszlJdbcTemplate extends JdbcTemplate {

        //数据库配置
        private DataSourceConfig dataSourceConfig;

        //映射策略
        @Setter
        @Accessors(chain = true)
        private NamingStrategy namingStrategy = NamingStrategy.underline_to_camel;
        public CszlJdbcTemplate(DataSource dataSource) {
            super(dataSource);
        }

        public CszlJdbcTemplate(DataSourceConfig dataSourceConfig) {
            this.dataSourceConfig = dataSourceConfig;
            this.setDataSource(new CszlDataSourceFactory(dataSourceConfig).getDataSource());
        }

        public DataSourceConfig getDataSourceConfig() {
            return dataSourceConfig;
        }


        public <R> List<R> getList(String sql, Class<R> r, Object... args) {
            return this.query(sql, args, (resultSet, i) -> rsToDto(r, resultSet));
        }

        public <R> R getOne(String sql, Class<R> r, Object... args) {
            return this.query(sql, args, resultSet -> {
                if (resultSet.next()) {
                    return rsToDto(r, resultSet);
                }
                return null;
            });
        }

        protected <R> R rsToDto(Class<R> r, ResultSet resultSet) {
            try {
                R r1 = r.newInstance();
                Class<?> r1Class = r1.getClass();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int j = 1; j <= columnCount; j++) {
                    String columnName = metaData.getColumnName(j);
                    String fileName = columnName;
                    if (namingStrategy.equals(NamingStrategy.underline_to_camel)) {
                        fileName = NamingStrategy.underlineToCamel(columnName);
                    }
                    Field field = null;
                    try {
                        field = r1Class.getDeclaredField(fileName);
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                    }
                    if (field != null) {
                        field.setAccessible(true);
                        Object object = resultSet.getObject(columnName);
                        if (object != null && object.getClass().isAssignableFrom(String.class)) {
                            field.set(r1, object);
                        } else {
                            // 判断不能去掉
                            field.set(r1, object == null ? null : String.valueOf(object));
                        }
                    }
                }
                return r1;
            } catch (Exception e) {
                throw new QueryException(e);
            }
        }

    }

    protected static class CszlDataSourceFactory implements DataSourceFactory {


        private DataSourceConfig dataSourceConfig;

        public CszlDataSourceFactory(DataSourceConfig dataSourceConfig) {
            this.dataSourceConfig = dataSourceConfig;
        }

        @Override
        public ConnectionProperties getConnectionProperties() {
            return null;
        }

        @SneakyThrows
        @Override
        public DataSource getDataSource() {
            return new CszlDriverDataSource(dataSourceConfig);
        }
    }

    protected static class CszlDriverDataSource extends AbstractDriverBasedDataSource {

        private Driver driver;

        public CszlDriverDataSource(DataSourceConfig dataSourceConfig) throws ClassNotFoundException {
            this.setUsername(dataSourceConfig.getUsername());
            this.setUrl(dataSourceConfig.getUrl());
            this.setPassword(dataSourceConfig.getPassword());
            this.driver = (Driver) BeanUtils.instantiateClass(Class.forName(dataSourceConfig.getDriverClass()));
        }

        @Override
        protected Connection getConnectionFromDriver(Properties properties) throws SQLException {
            return this.driver.connect(this.getUrl(), properties);
        }
    }

}

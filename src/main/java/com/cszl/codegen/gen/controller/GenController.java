package com.cszl.codegen.gen.controller;

import cn.hutool.core.date.DateUtil;
import com.cszl.codegen.base.constants.MetaQuerySql;
import com.cszl.codegen.base.controller.BaseController;
import com.cszl.codegen.base.db.DbTemplateFactory;
import com.cszl.codegen.base.enums.DataBaseType;
import com.cszl.codegen.base.enums.DbOperation;
import com.cszl.codegen.base.response.Result;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.entity.mysql.ColumnInfo;
import com.cszl.codegen.gen.entity.mysql.TableInfo;
import com.cszl.codegen.gen.enums.GenMethodName;
import com.cszl.codegen.gen.enums.QueryConditionRel;
import com.cszl.codegen.gen.enums.QueryRel;
import com.cszl.codegen.gen.service.GenCodeService;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 代码生成控制器
 * create by wdq on 2021/4/9 11:17
 */
@RestController
@RequestMapping(value = "gen")
public class GenController extends BaseController<GenCodeService> {

    /**
     * 查询可选创建方法
     *
     * @return
     */
    @GetMapping("listGenMethodName")
    public Result listGenMethodName() {
        return Result.ok().data(GenMethodName.values());
    }

    /**
     * 查询可选条件
     *
     * @return
     */
    @GetMapping("listQueryRels")
    public Result listQueryRels() {
        return Result.ok().data(QueryRel.values());
    }

    /**
     * 查询可选条件关联关系
     *
     * @return
     */
    @GetMapping("listQueryConditionRels")
    public Result listQueryConditionRels() {
        return Result.ok().data(QueryConditionRel.values());
    }

    /**
     * 查询表
     *
     * @return
     */
    @GetMapping("listTables")
    public Result listTables(DbTemplateFactory.CszlJdbcTemplate cszlJdbcTemplate) {
        List<TableInfo> list = cszlJdbcTemplate.getList(MetaQuerySql.USE_SQL.get(cszlJdbcTemplate.getDataSourceConfig().getDataBaseType()).get(DbOperation.queryTable), TableInfo.class, cszlJdbcTemplate.getDataSourceConfig().getDbName());
        if(cszlJdbcTemplate.getDataSourceConfig().getDataBaseType().equals(DataBaseType.POSTGRESQL)){
            list=list.stream().filter(m->!m.getTableName().startsWith("pg_") && !m.getTableName().startsWith("sql_")).collect(Collectors.toList());
        }
        return Result.ok().data(list);
    }

    /**
     * 查询字段
     *
     * @param cszlJdbcTemplate
     * @param tableName
     * @return
     */
    @GetMapping("listColumnByTableName")
    public Result listColumn(DbTemplateFactory.CszlJdbcTemplate cszlJdbcTemplate, String tableName) {
        List<ColumnInfo> columnInfos = cszlJdbcTemplate.getList(MetaQuerySql.USE_SQL.get(cszlJdbcTemplate.getDataSourceConfig().getDataBaseType()).get(DbOperation.queryColumn), ColumnInfo.class, cszlJdbcTemplate.getDataSourceConfig().getDbName(), tableName);
        return Result.ok().data((Serializable) columnInfos);
    }


    /**
     * 创建代码
     *
     * @param cszlJdbcTemplate
     * @return
     */
    @RequestMapping("/genCode")
    public void genCode(DbTemplateFactory.CszlJdbcTemplate cszlJdbcTemplate, @RequestBody GenerateConfig generateConfig, HttpServletResponse response) throws IOException {

        byte[] bytes = service.gen(generateConfig, cszlJdbcTemplate);
        response.reset();
        response.setHeader("Content-Disposition", String.format("attachment;filename=\"code_%s.zip\"", DateUtil.format(new Date(),"yyyyMMddHHmmss")));
        response.addHeader("Content-Length", "" + bytes.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(bytes, response.getOutputStream());

    }


}

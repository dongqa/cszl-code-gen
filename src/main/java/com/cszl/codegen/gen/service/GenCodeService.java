package com.cszl.codegen.gen.service;

import com.cszl.codegen.base.db.DbTemplateFactory;
import com.cszl.codegen.gen.config.GenerateConfig;

/**
 * 生成引擎
 * create by wdq on 2021/4/9 18:09
 */
public interface GenCodeService {

    /**
     * @param config
     * @return
     */
    byte[] gen(GenerateConfig config, DbTemplateFactory.CszlJdbcTemplate jdbcTemplate);
}

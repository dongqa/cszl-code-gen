package com.cszl.codegen.base.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

/**
 * create by wdq on 2021/4/9 16:40
 */
@Slf4j
public class PropertiesUtils {

    public final static Properties typeMapping = new Properties();

    static {
        try {
            typeMapping.load(PropertiesUtils.class.getResource("/type-mapping.properties").openStream());
        } catch (IOException e) {
            log.error("配置加载失败======type-mapping.properties", e);
        }
    }

}

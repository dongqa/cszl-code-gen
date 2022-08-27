package com.cszl.codegen.gen.service;

/**
 * 转换引擎
 * create by wdq on 2021/4/9 18:09
 */
public interface TransService {

    /**
     * json转javabean
     *
     * @param json
     * @return
     */
    Object json2JavaBeanHtml(String packageName, String json);
}

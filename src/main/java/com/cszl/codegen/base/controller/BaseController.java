package com.cszl.codegen.base.controller;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基础控制层
 * create by wdq on 2021/5/13 15:27
 */
public class BaseController<S> {
    @Autowired(required = false)
    protected S service;
}

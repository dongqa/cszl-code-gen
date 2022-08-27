package com.cszl.codegen.gen.controller;

import com.cszl.codegen.base.controller.BaseController;
import com.cszl.codegen.base.response.Result;
import com.cszl.codegen.gen.service.TransService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 转换控制器
 * create by wdq on 2021/4/9 11:17
 */
@RestController
@RequestMapping(value = "trans")
public class TransController extends BaseController<TransService> {


    @PostMapping("json2JavaBeanHtml")
    public Result json2JavaBeanHtml(@RequestBody Map<String,String> body) {
        return Result.ok().data(service.json2JavaBeanHtml(body.get("packageName"), body.get("json")));
    }

}

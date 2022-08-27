package com.cszl.codegen.base.advice;

import com.cszl.codegen.base.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * create by wdq on 2021/4/10 16:46
 */
@Slf4j
@ControllerAdvice
public class ExceptionHanlder {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result e(Throwable throwable) {
        log.error("", throwable);
        return Result.error(throwable.getMessage());
    }
}

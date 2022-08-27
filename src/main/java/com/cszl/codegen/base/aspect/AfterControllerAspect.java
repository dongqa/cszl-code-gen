package com.cszl.codegen.base.aspect;

import com.cszl.codegen.base.db.DbTemplateFactory;
import com.cszl.codegen.base.utils.CszlJdbcTemplateUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 销毁CszlJdbcTemplate
 * create by wdq on 2021/4/9 17:19
 */
@Aspect
@Component
public class AfterControllerAspect {

    @AfterReturning(value = "execution(* com.cszl.*.*.controller.*.*(..))", returning = "retVal")
    @AfterThrowing(value = "execution(* com.cszl.*.*.controller.*.*(..))")
    public void after(JoinPoint joinPoint, Object retVal) throws Throwable {
        DbTemplateFactory.CszlJdbcTemplate jdbcTemplate = CszlJdbcTemplateUtils.get();
        if (jdbcTemplate != null) {
            CszlJdbcTemplateUtils.remove();
        }
    }
}

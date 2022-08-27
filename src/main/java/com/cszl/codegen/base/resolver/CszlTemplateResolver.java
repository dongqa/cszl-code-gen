package com.cszl.codegen.base.resolver;

import com.cszl.codegen.base.db.DataSourceConfig;
import com.cszl.codegen.base.db.DbTemplateFactory;
import com.cszl.codegen.base.utils.BeanUtil;
import com.cszl.codegen.base.utils.CszlJdbcTemplateUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

/**
 * create by wdq on 2021/4/9 16:55
 * @author wdq
 */
public class CszlTemplateResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return DbTemplateFactory.CszlJdbcTemplate.class.isAssignableFrom(methodParameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        final HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();

        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(request.getHeader("Db-Url"));
        dataSourceConfig.setUsername(request.getHeader("Db-Username"));
        dataSourceConfig.setPassword(request.getHeader("Db-Password"));

        DbTemplateFactory.CszlJdbcTemplate jdbcTemplate = DbTemplateFactory.getInstance(dataSourceConfig);

        CszlJdbcTemplateUtils.setCszlJdbcTemplate(jdbcTemplate);

        return jdbcTemplate;
    }
}

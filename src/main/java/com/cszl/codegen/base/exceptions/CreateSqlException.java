package com.cszl.codegen.base.exceptions;

/**
 * 创建sql异常
 * create by wdq on 2021/4/9 15:15
 * @author wdq
 */
public class CreateSqlException extends RuntimeException {

    private static final long serialVersionUID = 672755941247298703L;

    public CreateSqlException(String message) {
        super(message);
    }

    public CreateSqlException(Throwable cause) {
        super(cause);
    }
}

package com.cszl.codegen.base.exceptions;

/**
 * 查询异常
 * create by wdq on 2021/4/9 15:15
 * @author wdq
 */
public class QueryException extends RuntimeException {

    private static final long serialVersionUID = 672755946437298703L;

    public QueryException(String message) {
        super(message);
    }

    public QueryException(Throwable cause) {
        super(cause);
    }
}

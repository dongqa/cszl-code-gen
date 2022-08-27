package com.cszl.codegen.base.exceptions;

/**
 * 不支持异常
 * create by wdq on 2021/4/9 11:16
 */
public class UnsupportDataBaseException extends RuntimeException {

    private static final long serialVersionUID = 4919645198520196137L;

    public UnsupportDataBaseException() {
    }

    public UnsupportDataBaseException(String message) {
        super(message);
    }
}

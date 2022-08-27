package com.cszl.codegen.base.response;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回结果集
 * @author wdq
 */
public class Result extends HashMap<String, Object> {

    private static final long serialVersionUID = 6940533409246502555L;

    private final static String CODE = "code";

    private final static String MSG = "message";

    public static Result ok(int errorCode, String errorMessage) {
        return new Result().put(CODE, ErrorCodes.SUCCESS.code).put(MSG, errorMessage);
    }

    public static Result ok() {
        return new Result().put(CODE, ErrorCodes.SUCCESS.code).put(MSG, "");
    }

    public <T extends Serializable> Result data(T t) {
        return ok().put("data", t);
    }

    public Result data(Object object) {
        return data((Serializable) object);
    }

    public Result addAll(Map<String, Object> map) {
        this.putAll(map);
        return this;
    }

    public static Result error(int errorCode, String errorMessage) {
        return new Result().put(CODE, errorCode).put(MSG, errorMessage);
    }

    public static Result error(String errorMessage) {
        return new Result().put(CODE, ErrorCodes.ERROR.code).put(MSG, errorMessage);
    }

    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public enum ErrorCodes {

        //20000成功
        //50000失败
        SUCCESS(20000),
        ERROR(50000);

        public int code;

        ErrorCodes(int code) {
            this.code = code;
        }
    }
}

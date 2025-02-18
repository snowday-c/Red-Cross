package com.example.redcross.common;

import lombok.Data;

@Data
public class Result {
    private final static String SUCCESS = "0";
    private final static String ERROR = "-1";
    private String code;
    private String message;
    private Object data;

    public  static Result success() {
        Result result = new Result();
        result.setCode(SUCCESS);
        return result;
    }
    public  static Result success(Object data) {
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setData(data);
        return result;
    }
    public  static Result error(String message) {
        Result result = new Result();
        result.setCode(ERROR);
        result.setMessage(message);
        return result;
    }

}

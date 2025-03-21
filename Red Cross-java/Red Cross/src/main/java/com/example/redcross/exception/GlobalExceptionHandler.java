package com.example.redcross.exception;

import com.example.redcross.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice(basePackages="com.example.redcross.controller")
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //统一异常处理@ExceptionHandler,主要用于Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(HttpServletRequest request, Exception e){
        log.error("异常信息：",e);
        return Result.error("系统异常");
    }

    @ExceptionHandler(UserException.class)
    @ResponseBody
    public Result userError(HttpServletRequest request, UserException e){
        return Result.error(e.getMsg());
    }

    @ExceptionHandler(MessageException.class)
    @ResponseBody
    public Result messageError(HttpServletRequest request, MessageException e){
        return Result.error(e.getMsg());
    }
}
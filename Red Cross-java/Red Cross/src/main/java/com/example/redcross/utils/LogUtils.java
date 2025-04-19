package com.example.redcross.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 */
public class LogUtils {
    
    /**
     * 获取业务日志记录器
     * @param clazz 类
     * @return 日志记录器
     */
    public static Logger getBusinessLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
    
    /**
     * 获取系统日志记录器
     * @return 日志记录器
     */
    public static Logger getSystemLogger() {
        return LoggerFactory.getLogger("SYSTEM_LOG");
    }
    
    /**
     * 获取异常日志记录器
     * @return 日志记录器
     */
    public static Logger getExceptionLogger() {
        return LoggerFactory.getLogger("EXCEPTION_LOG");
    }
    
    /**
     * 获取操作日志记录器
     * @return 日志记录器
     */
    public static Logger getOperationLogger() {
        return LoggerFactory.getLogger("OPERATION_LOG");
    }
}
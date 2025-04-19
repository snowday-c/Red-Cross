package com.example.redcross.aspect;

import com.example.redcross.utils.LogUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    
    private final Logger logger = LogUtils.getOperationLogger();
    
    @Pointcut("execution(* com.example.redcross.controller.*.*(..))")
    public void controllerLog() {}
    
    @Before("controllerLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            logger.info("请求URL: {}", request.getRequestURL().toString());
            logger.info("请求方法: {}", request.getMethod());
            logger.info("请求IP: {}", request.getRemoteAddr());
            logger.info("调用方法: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            logger.info("方法参数: {}", Arrays.toString(joinPoint.getArgs()));
        }
    }
    
    @Around("controllerLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        logger.info("方法执行时间: {}ms", endTime - startTime);
        return result;
    }
    
    @AfterReturning(returning = "result", pointcut = "controllerLog()")
    public void doAfterReturning(Object result) {
        logger.info("返回结果: {}", result);
    }
    
    @AfterThrowing(throwing = "e", pointcut = "controllerLog()")
    public void doAfterThrowing(Throwable e) {
        Logger exceptionLogger = LogUtils.getExceptionLogger();
        exceptionLogger.error("发生异常: {}", e.getMessage(), e);
    }
}
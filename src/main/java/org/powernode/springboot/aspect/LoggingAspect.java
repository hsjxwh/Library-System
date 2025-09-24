package org.powernode.springboot.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
    @Around("execution(* org.powernode.springboot.service..*(..)) ||"+
        "execution(* org.powernode.springboot.controller..*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger= LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        logger.debug("执行开始: {}.{}()", className, methodName);
        long startTime = System.currentTimeMillis();
        try{
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            logger.debug("执行成功: {}.{}() - 耗时: {}ms",
                    className, methodName, (endTime - startTime));

            return result;
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("执行失败: {}.{}() - 耗时: {}ms - 错误: {}",
                    className, methodName, (endTime - startTime), e.getMessage());
            throw e;
        }
    }


}

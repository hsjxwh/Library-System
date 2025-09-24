package org.powernode.springboot.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.powernode.springboot.annotation.TransactionFail;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Aspect
@Component
//处理增删改事物中有一个增删改业务出现影响行数为0的结果是应该做的操作
public class TransactionAspect {
    @Around("@annotation(transactionFail)")
    public Object checkTransaction(ProceedingJoinPoint joinPoint, TransactionFail transactionFail) throws Throwable {
        Object result = joinPoint.proceed();
        //一旦出现增删改操作影响行数为零，应该进行事物回滚，并在遇到某些特定的方法的时候打印日志
        if ((result instanceof Integer integer && integer <= 0)) {
            //标记当前事物为回滚状态
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        return result;
    }
}



package org.powernode.springboot.exception;

//当增删改的影响行数为0的时候触发的异常
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

package org.powernode.springboot.exception;
//当前端传来错误的数据是抛出的异常
public class ErrorRequestParamError extends RuntimeException {
    public ErrorRequestParamError(String message) {
        super(message);
    }
}

package org.powernode.springboot.exception;
//登录token失效异常
public class IllegalLoginTokenError extends RuntimeException {
    public IllegalLoginTokenError(String message) {
        super(message);
    }
}

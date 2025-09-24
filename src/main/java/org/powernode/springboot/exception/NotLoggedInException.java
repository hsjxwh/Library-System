package org.powernode.springboot.exception;
//用户未登录异常
public class NotLoggedInException extends  RuntimeException{
    public NotLoggedInException(String message) {
        super(message);
    }
}

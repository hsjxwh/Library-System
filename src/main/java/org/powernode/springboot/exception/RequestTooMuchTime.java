package org.powernode.springboot.exception;
//请求发太多次的时候
public class RequestTooMuchTime extends RuntimeException{
    public RequestTooMuchTime(String message){
        super(message);
    }
}

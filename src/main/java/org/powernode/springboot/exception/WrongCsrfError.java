package org.powernode.springboot.exception;
//当验证csrf的时候发现jwt中的csrf和请求头中的csrf不一样的时候报的错误
public class WrongCsrfError extends RuntimeException{
    public WrongCsrfError(String message){
        super(message);
    }
}

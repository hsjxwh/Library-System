package org.powernode.springboot.exception;
//前端缺少必要信息返回的异常
public class LackOfInfo extends RuntimeException{
    public LackOfInfo(String message){
        super(message);
    }
}

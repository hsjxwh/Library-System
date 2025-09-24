package org.powernode.springboot.exception;
//验证是否时管理员时，不是时所报的异常
public class HaveNotAdminAuthority extends RuntimeException{
    public HaveNotAdminAuthority(String message){
        super(message);
    }
}

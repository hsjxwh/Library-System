package org.powernode.springboot.service.database.service.redis;

public interface RegisterService {
    void setVerifyCode(String email,String token);
    boolean hasVerifyCode(String email);
    boolean checkVerifyCode(String email,String token);
}

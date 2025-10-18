package org.powernode.springboot.service.mail;

public interface LoginService {
    boolean sendVerification(String email);
}

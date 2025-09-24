package org.powernode.springboot.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorityError {
    private String errorType;
    AuthorityError(String errorType) {
        this.errorType = errorType;
    }

    public static AuthorityError treatExpiredJwtException(){
        return new AuthorityError("登陆过期，请重新登录");
    }

    public static AuthorityError treatSignatureException(){
        return new AuthorityError("权限错误，请重新登录");
    }

    public static AuthorityError treatNotLoggedInException() {
        return new AuthorityError("还未登录，请登陆后进行操作");
    }
}

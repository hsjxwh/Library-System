package org.powernode.springboot;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.powernode.springboot.exception.*;
import org.powernode.springboot.mapper.database.BalanceNotEnoughError;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常
@ControllerAdvice
public class GlobalErrorResponse {
    //登录过期报的错误
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public ResponseEntity<String> handleWithExpiredJwtException(){
        AuthorityError error= AuthorityError.treatExpiredJwtException();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error.getErrorType());
    }

    //jwt错误报的错误
    @ExceptionHandler(SignatureException.class)
    @ResponseBody
    public ResponseEntity<String> handleWithSignatureException(){
        AuthorityError error= AuthorityError.treatSignatureException();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error.getErrorType());
    }

    //未登录异常
    @ExceptionHandler(NotLoggedInException.class)
    @ResponseBody
    public ResponseEntity<String> handleWithIllegalArgumentException(){
        AuthorityError error=AuthorityError.treatNotLoggedInException();
        return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(error.getErrorType());
    }

    @ExceptionHandler(WrongCsrfError.class)
    @ResponseBody
    public ResponseEntity<String> handleWithWrongCsrfError(){
        return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("权限错误，请重新登录");
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<String> handleTypeMismatch(Exception e) {
        return ResponseEntity.status(400).body("参数格式错误：" + e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<String> handleMissingParam(Exception e) {
        return ResponseEntity.status(400).body("缺少参数：" + e.getMessage());
    }

    @ExceptionHandler(HaveNotAdminAuthority.class)
    @ResponseBody
    public ResponseEntity<String> handleWithHaveNotAdminAuthority(Exception e) {
        return ResponseEntity.status(403).body("您没有管理员权限");
    }

    @ExceptionHandler(FileError.class)
    @ResponseBody
    public ResponseEntity<String> handleWithFileError(Exception e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(SourceRepeatError.class)
    @ResponseBody
    public ResponseEntity<String> handleWithRepeatError(Exception e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }

    @ExceptionHandler(RenewManyTimeError.class)
    @ResponseBody
    public ResponseEntity<String> handleWithRenewManyTimeError(Exception e) {
        return ResponseEntity.status(403).body(e.getMessage());
    }

    @ExceptionHandler(InsufficientCreditError.class)
    @ResponseBody
    public ResponseEntity<String> handleWithInsufficientCreditError(Exception e) {
        return ResponseEntity.status(403).body(e.getMessage());
    }

    @ExceptionHandler(BalanceNotEnoughError.class)
    @ResponseBody
    public ResponseEntity<String> handleWithBalanceNotEnoughError(Exception e) {
        return ResponseEntity.status(403).body(e.getMessage());
    }

    @ExceptionHandler(IllegalLoginTokenError.class)
    @ResponseBody
    public ResponseEntity<String> handleWithIllegalLoginTokenError(Exception e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler(RequestTooMuchTime.class)
    @ResponseBody
    public ResponseEntity<String> handleWithRequestTooMuchTime(Exception e) {
        return ResponseEntity.status(429).body(e.getMessage());
    }
}

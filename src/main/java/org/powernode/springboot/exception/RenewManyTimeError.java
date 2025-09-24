package org.powernode.springboot.exception;
//当一次借书记录中的某本书单本超过两次的时候抛出的异常
public class RenewManyTimeError extends RuntimeException {
    public RenewManyTimeError(String message) {
        super(message);
    }
}

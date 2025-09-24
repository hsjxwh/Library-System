package org.powernode.springboot.exception;

public class InsufficientCreditError extends RuntimeException {
    public InsufficientCreditError(String message) {
        super(message);
    }
}

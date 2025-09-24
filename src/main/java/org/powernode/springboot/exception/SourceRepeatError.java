package org.powernode.springboot.exception;
public class SourceRepeatError extends RuntimeException {
    public SourceRepeatError(String message) {
        super(message);
    }
}

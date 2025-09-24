package org.powernode.springboot.mapper.database;

public class BalanceNotEnoughError extends RuntimeException {
    public BalanceNotEnoughError(String message) {
        super(message);
    }
}

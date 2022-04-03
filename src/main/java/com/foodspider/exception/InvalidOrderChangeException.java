package com.foodspider.exception;

public class InvalidOrderChangeException extends RuntimeException {
    public InvalidOrderChangeException(String message) {
        super(message);
    }
}

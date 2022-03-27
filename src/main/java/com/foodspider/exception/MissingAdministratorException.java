package com.foodspider.exception;

public class MissingAdministratorException extends RuntimeException {
    public MissingAdministratorException(String message) {
        super(message);
    }
}

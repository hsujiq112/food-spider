package com.foodspider.exception;

public class MissingCustomerException extends RuntimeException{
    public MissingCustomerException(String message) {
        super(message);
    }
}

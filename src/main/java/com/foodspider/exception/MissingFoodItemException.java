package com.foodspider.exception;

public class MissingFoodItemException extends RuntimeException {
    public MissingFoodItemException(String message) {
        super(message);
    }
}

package com.praveen.webflux.sec061.exception;

public class InvalidOperationException extends RuntimeException {
    
    public InvalidOperationException(String message) {
        super(message);
    }
}

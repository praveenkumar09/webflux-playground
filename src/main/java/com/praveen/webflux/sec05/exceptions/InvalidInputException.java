package com.praveen.webflux.sec05.exceptions;

public class InvalidInputException extends RuntimeException{

    public InvalidInputException(String message) {
        super(message);
    }
}

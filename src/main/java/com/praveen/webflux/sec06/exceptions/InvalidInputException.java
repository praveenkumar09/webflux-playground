package com.praveen.webflux.sec06.exceptions;

public class InvalidInputException extends RuntimeException{

    public InvalidInputException(String message) {
        super(message);
    }
}

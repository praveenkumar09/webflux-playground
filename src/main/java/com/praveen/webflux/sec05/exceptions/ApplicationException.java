package com.praveen.webflux.sec05.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationException {

    public static <T> Mono<T> customerNotFound(Integer id) {
        return Mono.error(new CustomerNotFoundException(id));
    }

    public static <T> Mono<T> missingName(){
        return Mono.error(new InvalidInputException("Name is required"));
    }

    public static <T> Mono<T> missingEmail(){
        return Mono.error(new InvalidInputException("Email is required"));
    }

    public static <T> Mono<T> missingMandatoryFields(){
        return Mono.error(new InvalidInputException("Mandatory fields are required"));
    }
}

package com.praveen.webflux.sec04.validator;

import com.praveen.webflux.sec04.dto.CustomerDto;
import com.praveen.webflux.sec04.exceptions.ApplicationException;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    private static Predicate<CustomerDto> hasName(){
        return dto -> dto.name() != null
                && !dto.name().isBlank();
    }

    private static Predicate<CustomerDto> hasEmail(){
        return dto -> dto.email() != null
                && !dto.email().isBlank()
                && !dto.email().contains("@");
    }

    public static UnaryOperator<Mono<CustomerDto>> validate(){
        return mono -> mono
                .filter(hasName())
                .switchIfEmpty(ApplicationException.missingName())
                .filter(hasEmail())
                .switchIfEmpty(ApplicationException.missingEmail());
    }
}

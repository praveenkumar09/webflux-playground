package com.praveen.webflux.sec04.validator;

import com.praveen.webflux.sec04.dto.CustomerDto;
import com.praveen.webflux.sec04.exceptions.ApplicationException;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    private static Predicate<CustomerDto> hasName(){
        return dto -> Objects.nonNull(dto.name());
    }

    private static Predicate<CustomerDto> hasEmail(){
        return dto -> Objects.nonNull(dto.email())
                && dto.email().contains("@");
    }

    public static UnaryOperator<Mono<CustomerDto>> validate(){
        return mono -> mono
                .filter(hasName())
                .switchIfEmpty(ApplicationException.missingName())
                .filter(hasEmail())
                .switchIfEmpty(ApplicationException.missingEmail());
    }
}

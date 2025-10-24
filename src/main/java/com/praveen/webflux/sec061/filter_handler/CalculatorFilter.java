package com.praveen.webflux.sec061.filter_handler;

import com.praveen.webflux.sec061.error_handler.ErrorHandlerFunction;
import com.praveen.webflux.sec061.exception.InvalidOperationException;
import com.praveen.webflux.sec061.routes_handler.CalculatorHandlerFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class CalculatorFilter {

    private final Logger log = LoggerFactory.getLogger(CalculatorFilter.class);
    private final ErrorHandlerFunction errorHandlerFunction;

    @Autowired
    public CalculatorFilter(ErrorHandlerFunction errorHandlerFunction) {
        this.errorHandlerFunction = errorHandlerFunction;
    }

    public Mono<ServerResponse> checkHeader(ServerRequest request, HandlerFunction<ServerResponse> next) {
        String headerVal = request.headers().firstHeader("operation");
        log.debug("Received operation header: {}", headerVal);
        if (headerVal != null && (headerVal.equals("+")
                || headerVal.equals("-")
                || headerVal.equals("*")
                || headerVal.equals("/"))) {
            log.info("Valid operation header: {}", headerVal);
            return next.handle(request);
        } else {
            log.error("Invalid operation header: {}", headerVal);
            InvalidOperationException exception = new InvalidOperationException("Invalid operation header");
            return errorHandlerFunction.handleBadRequestError(exception, request);

        }
    }
}

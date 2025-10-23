package com.praveen.webflux.sec00.filter_handler;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CalculatorFilter {

    public Mono<ServerResponse> checkHeader(ServerRequest request, HandlerFunction<ServerResponse> next) {
        String headerVal = request.headers().firstHeader("operation");
        if (headerVal != null && (headerVal.equals("+")
                || headerVal.equals("-")
                || headerVal.equals("*")
                || headerVal.equals("/"))) {
            return next.handle(request);
        } else {
            return ServerResponse.badRequest().build();
        }
    }
}

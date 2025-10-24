package com.praveen.webflux.sec061.request_predicates;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class CalculatorRequestPredicates {

    public boolean isValidCalculatorRequest(ServerRequest request) {
        int a = Integer.parseInt(request.pathVariable("a"));
        int b = Integer.parseInt(request.pathVariable("b"));
        return a > b && b != 0;
    }
}

package com.praveen.webflux.sec061.routes_handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CalculatorHandlerFunction {
    private static final Logger log = LoggerFactory.getLogger(CalculatorHandlerFunction.class);

    public Mono<ServerResponse> process(ServerRequest request) {
        int a = Integer.parseInt(request.pathVariable("a"));
        int b = Integer.parseInt(request.pathVariable("b"));
        String header = request
                .headers()
                .firstHeader("operation");
        return Mono.fromSupplier(() -> {
                    assert header != null;
                    return calculate(a, b, header);
                })
                .flatMap(result -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(result));

    }

    private double calculate(int a, int b, String operation) {
        return switch (operation) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield (double) a / b;
            }
            default -> throw new IllegalArgumentException("Invalid operation: " + operation);
        };
    }

}

package com.praveen.webflux.sec061.routes;


import com.praveen.webflux.sec061.error_handler.ErrorHandlerFunction;
import com.praveen.webflux.sec061.exception.InvalidOperationException;
import com.praveen.webflux.sec061.filter_handler.CalculatorFilter;
import com.praveen.webflux.sec061.routes_handler.CalculatorHandlerFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class CalculatorRouteHandler {

    private final CalculatorHandlerFunction calculatorHandlerFunction;

    private final ErrorHandlerFunction errorHandlerFunction;

    private final CalculatorFilter calculatorFilter;


    @Autowired
    public CalculatorRouteHandler(
            CalculatorHandlerFunction calculatorHandlerFunction,
            ErrorHandlerFunction errorHandlerFunction,
            CalculatorFilter calculatorFilter
    ) {
        this.calculatorHandlerFunction = calculatorHandlerFunction;
        this.errorHandlerFunction = errorHandlerFunction;
        this.calculatorFilter = calculatorFilter;
    }

    @Bean
    public RouterFunction<ServerResponse> calculatorRoutes(){
        RequestPredicate requestPredicate = (request -> {
                    int a = Integer.parseInt(request.pathVariable("a"));
                    int b = Integer.parseInt(request.pathVariable("b"));
                    return a > b && b != 0;
                });
        return RouterFunctions.route()
                .GET("/calculator/{a}/{b}",
                        requestPredicate,
                        calculatorHandlerFunction::process)
                .filter(calculatorFilter::checkHeader)
                .onError(InvalidOperationException.class, errorHandlerFunction::handleBadRequestError)
                .onError(RuntimeException.class,errorHandlerFunction::handleServerError)
                .build();
    }

}

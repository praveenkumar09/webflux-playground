package com.praveen.webflux.sec061.routes;


import com.praveen.webflux.sec061.error_handler.ErrorHandlerFunction;
import com.praveen.webflux.sec061.exception.InvalidOperationException;
import com.praveen.webflux.sec061.filter_handler.CalculatorFilter;
import com.praveen.webflux.sec061.request_predicates.CalculatorRequestPredicates;
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

    private final CalculatorRequestPredicates calculatorRequestPredicates;


    @Autowired
    public CalculatorRouteHandler(
            CalculatorHandlerFunction calculatorHandlerFunction,
            ErrorHandlerFunction errorHandlerFunction,
            CalculatorFilter calculatorFilter,
            CalculatorRequestPredicates calculatorRequestPredicates
    ) {
        this.calculatorHandlerFunction = calculatorHandlerFunction;
        this.errorHandlerFunction = errorHandlerFunction;
        this.calculatorFilter = calculatorFilter;
        this.calculatorRequestPredicates = calculatorRequestPredicates;
    }

    @Bean
    public RouterFunction<ServerResponse> calculatorRoutes(){
        return RouterFunctions.route()
                .GET("/calculator/{a}/{b}",
                        calculatorRequestPredicates::isValidCalculatorRequest,
                        calculatorHandlerFunction::process)
                .filter(calculatorFilter::checkHeader)
                .onError(InvalidOperationException.class, errorHandlerFunction::handleBadRequestError)
                .onError(RuntimeException.class,errorHandlerFunction::handleServerError)
                .build();
    }

}

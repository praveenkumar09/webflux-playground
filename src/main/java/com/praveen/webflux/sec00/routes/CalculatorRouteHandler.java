package com.praveen.webflux.sec00.routes;


import com.praveen.webflux.sec00.error_handler.ErrorHandlerFunction;
import com.praveen.webflux.sec00.filter_handler.CalculatorFilter;
import com.praveen.webflux.sec00.routes_handler.CalculatorHandlerFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.*;

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
                .onError(WebClientResponseException.BadRequest.class, errorHandlerFunction::handleBadRequestError)
                .onError(RuntimeException.class,errorHandlerFunction::handleServerError)
                .filter(calculatorFilter::checkHeader)
                .build();
    }

}

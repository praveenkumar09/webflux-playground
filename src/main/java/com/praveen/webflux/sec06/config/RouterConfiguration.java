package com.praveen.webflux.sec06.config;


import com.praveen.webflux.sec06.advice.ApplicationExceptionHandler;
import com.praveen.webflux.sec06.exceptions.CustomerNotFoundException;
import com.praveen.webflux.sec06.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    private final CustomerRequestHandler customerRequestHandler;

    private final ApplicationExceptionHandler applicationExceptionHandler;

    @Autowired
    public RouterConfiguration(
            CustomerRequestHandler customerRequestHandler,
            ApplicationExceptionHandler applicationExceptionHandler
    ) {
        this.customerRequestHandler = customerRequestHandler;
        this.applicationExceptionHandler = applicationExceptionHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        return RouterFunctions
                .route()
                .path("/customers", builder ->
                        builder
                                .GET("/all", customerRequestHandler::allCustomers)
                                .GET("/all/paginated", customerRequestHandler::allCustomersPaginated)
                                .GET("/{id}", customerRequestHandler::getCustomer)
                                .POST("/save", customerRequestHandler::saveCustomer)
                                .PUT("/update/{id}", customerRequestHandler::updateCustomer)
                                .DELETE("/delete/{id}", customerRequestHandler::deleteCustomer)
                                .onError(CustomerNotFoundException.class, this.applicationExceptionHandler::handleException)
                                .onError(InvalidInputException.class, this.applicationExceptionHandler::handleException)
                )
                .build();
    }
}

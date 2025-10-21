package com.praveen.webflux.sec06.config;

import com.praveen.webflux.sec06.dto.CustomerDto;
import com.praveen.webflux.sec06.exceptions.ApplicationException;
import com.praveen.webflux.sec06.service.CustomerService;
import com.praveen.webflux.sec06.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CustomerRequestHandler {

    private final CustomerService customerService;

    @Autowired
    public CustomerRequestHandler(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Mono<ServerResponse> allCustomers(ServerRequest request) {
        return this
                .customerService
                .getAllCustomers()
                .as(customerDtoFlux ->
                        ServerResponse
                        .ok()
                        .body(customerDtoFlux, CustomerDto.class));
    }

    public Mono<ServerResponse> getCustomer(ServerRequest request){
        Integer id = Integer.valueOf(request.pathVariable("id"));
        return this
                .customerService
                .getCustomerById(id)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .flatMap(customerDto ->
                        ServerResponse
                        .ok()
                        .bodyValue(customerDto));
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request){
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(this
                        .customerService::saveCustomer)
                .flatMap(customerDto ->
                        ServerResponse
                        .ok()
                        .bodyValue(customerDto));
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request){
        int id = Integer.parseInt(request.pathVariable("id"));
        return request
                .bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(validatedCustomerDtoMono ->
                        this.customerService
                                .updateCustomer(id,validatedCustomerDtoMono))
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .flatMap(customerDto ->
                        ServerResponse
                        .ok()
                        .bodyValue(customerDto));
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request){
        int id = Integer.parseInt(request
                .pathVariable("id"));
        return this
                .customerService
                .deleteCustomer(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .flatMap(deleted -> deleted ? ServerResponse.ok().build() : ServerResponse.notFound().build());
    }
}

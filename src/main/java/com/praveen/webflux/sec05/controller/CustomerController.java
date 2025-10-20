package com.praveen.webflux.sec05.controller;

import com.praveen.webflux.sec05.dto.CustomerDto;
import com.praveen.webflux.sec05.exceptions.ApplicationException;
import com.praveen.webflux.sec05.service.CustomerService;
import com.praveen.webflux.sec05.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);


    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CustomerDto> getAllCustomers() {
        return this
                .customerService
                .getAllCustomers();
    }


    @GetMapping(value = "/all/paginated",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CustomerDto> getAllCustomersByPagination(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "3") Integer pageSize
    ) {
        return this
                .customerService
                .getAllCustomers(pageNo, pageSize);
    }

    @GetMapping(value = "{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CustomerDto> getCustomerById(@PathVariable Integer id) {
        return Mono
                .just(id)
                .transform(RequestValidator.validateMandatoryFields())
                .flatMap(this.customerService::getCustomerById)
                .switchIfEmpty(ApplicationException.customerNotFound(id));
    }

    @PostMapping(value="/save", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> customerDtoMono){
        return customerDtoMono
                .transform(RequestValidator.validate())
                .as(this.customerService::saveCustomer);
    }

    @PutMapping(value="/update/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CustomerDto> updateCustomer(@PathVariable Integer id,
                                            @RequestBody Mono<CustomerDto> customerDto){
        return Mono
                .just(id)
                .transform(RequestValidator.validateMandatoryFields())
                .flatMap(ID -> customerDto)
                .transform(RequestValidator.validate())
                .as(customerDtoMono ->
                        this
                                .customerService
                                .updateCustomer(id, customerDtoMono))
                .switchIfEmpty(ApplicationException.customerNotFound(id));
    }

    @DeleteMapping(value="/delete/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Void> deleteCustomer(@PathVariable Integer id){
        return Mono
                .just(id)
                .transform(RequestValidator.validateMandatoryFields())
                .as(ID ->
                        this
                                .customerService
                                .deleteCustomer(id))
                .filter(b -> b)
                .switchIfEmpty(ApplicationException.customerNotFound(id))
                .then();
    }
}

package com.praveen.webflux.sec03.controller;

import com.praveen.webflux.sec03.dto.CustomerDto;
import com.praveen.webflux.sec03.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {

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
    public Mono<ResponseEntity<CustomerDto>> getCustomerById(@PathVariable String id) {
        return this
                .customerService
                .getCustomerById(Integer.parseInt(id))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(value="/save", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> customerDtoMono){
        return this
                .customerService
                .saveCustomer(customerDtoMono);
    }

    @PutMapping(value="/update/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ResponseEntity<CustomerDto>> updateCustomer(@PathVariable String id,
                                            @RequestBody Mono<CustomerDto> customerDtoMono){
        return this
                .customerService
                .updateCustomer(Integer.parseInt(id), customerDtoMono)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value="/delete/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable String id){
        return this
                .customerService
                .deleteCustomer(Integer.parseInt(id))
                .filter(b -> b)
                .map(b -> ResponseEntity
                        .ok()
                        .<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

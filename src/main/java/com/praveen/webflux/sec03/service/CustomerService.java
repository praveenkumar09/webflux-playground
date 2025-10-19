package com.praveen.webflux.sec03.service;

import com.praveen.webflux.sec03.dto.CustomerDto;
import com.praveen.webflux.sec03.entity.Customer;
import com.praveen.webflux.sec03.mapper.EntityDtoMapper;
import com.praveen.webflux.sec03.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Flux<CustomerDto> getAllCustomers() {
        return this.customerRepository.findAll()
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> getCustomerById(Integer id) {
        return this.customerRepository.findById(id)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> customerDtoMono) {
        return customerDtoMono
                .map(EntityDtoMapper::toEntity)
                .flatMap(this.customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> updateCustomer(Integer id,
                                            Mono<CustomerDto> customerDtoMono){
        return this.customerRepository
                .findById(id)
                .flatMap(entity -> customerDtoMono)
                .map(EntityDtoMapper::toEntity)
                .doOnNext(entity -> entity.setId(id))
                .flatMap(this.customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Boolean> deleteCustomer(Integer id){
        return this.customerRepository
                .deleteCustomerById(id);
    }


}

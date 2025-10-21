package com.praveen.webflux.sec06.service;

import com.praveen.webflux.sec06.dto.CustomerDto;
import com.praveen.webflux.sec06.mapper.EntityDtoMapper;
import com.praveen.webflux.sec06.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Flux<CustomerDto> getAllCustomers() {
        return this.customerRepository.findAll()
                .map(EntityDtoMapper::toDto);
    }


    public Flux<CustomerDto> getAllCustomers(Integer pageNo, Integer pageSize) {
        return this.customerRepository.findBy(PageRequest.of(pageNo-1, pageSize))
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
                .doOnNext(entity -> log.info("Found entity: {}", entity))
                .doOnError(error -> log.error("Error finding entity: ", error))
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Customer with id {} not found!", id);
                    return Mono.empty();
                }))
                .flatMap(entity -> customerDtoMono
                            .map(dto -> {
                                entity.setEmail(dto.email());
                                entity.setName(dto.name());
                                return entity;
                            })
                )
                .flatMap(this.customerRepository::save)
                .doOnNext(saved -> log.info("Saved entity: {}", saved))
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Boolean> deleteCustomer(Integer id){
        return this.customerRepository
                .deleteCustomerById(id);
    }


}

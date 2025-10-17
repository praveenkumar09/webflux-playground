package com.praveen.webflux.sec02.repository;

import com.praveen.webflux.sec02.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

    Flux<Customer> findByEmail(String email);

    Flux<Customer> findByName(String name);

    Flux<Customer> findByNameAndEmail(String name, String email);

    Flux<Customer> findByEmailEndingWith(String email);
}

package com.praveen.webflux.sec08.repository;

import com.praveen.webflux.sec08.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends
        ReactiveCrudRepository<Product, Integer> {
}

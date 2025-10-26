package com.praveen.webflux.sec09.repository;

import com.praveen.webflux.sec09.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends
        ReactiveCrudRepository<Product, Integer> {
}

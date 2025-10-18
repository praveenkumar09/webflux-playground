package com.praveen.webflux.tests.sec02;

import com.praveen.webflux.sec02.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec02ProductRepositoryTest extends AbstractTest{
    private static final Logger log = LoggerFactory.getLogger(Lec02ProductRepositoryTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByPriceBetween() {
        this.productRepository
                .findByPriceBetween(100,1000)
                .as(StepVerifier::create)
                .expectNextCount(7)
                .verifyComplete();
    }

}

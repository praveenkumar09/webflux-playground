package com.praveen.webflux.tests.sec02;

import com.praveen.webflux.sec02.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

public class Lec02ProductRepositoryTest extends AbstractTest{
    private static final Logger log = LoggerFactory.getLogger(Lec02ProductRepositoryTest.class);

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByPriceBetween() {
        this.productRepository
                .findByPriceBetween(100,1000)
                .doOnNext(product -> log.info("Product: {}", product))
                .as(StepVerifier::create)
                .expectNextCount(7)
                .verifyComplete();
    }

    @Test
    public void findByPageable(){
        this.productRepository
                .findBy(PageRequest.of(0,
                        3)
                        .withSort(Sort
                                .by("price")
                                .ascending())
                )
                .doOnNext(product -> log.info("Product: {}", product))
                .as(StepVerifier::create)
                .assertNext(product -> Assertions.assertEquals(200, product.getPrice()))
                .assertNext(product -> Assertions.assertEquals(250, product.getPrice()))
                .assertNext(product -> Assertions.assertEquals(300, product.getPrice()))
                .verifyComplete();
    }

}

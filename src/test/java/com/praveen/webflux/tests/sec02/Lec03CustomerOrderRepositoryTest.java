package com.praveen.webflux.tests.sec02;

import com.praveen.webflux.sec02.repository.CustomerOrderRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec03CustomerOrderRepositoryTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(Lec03CustomerOrderRepositoryTest.class);

    @Autowired
    private CustomerOrderRepository customerOrderRepository;


    @Test
    public void testProductsOrderedByCustomer(){
        this.customerOrderRepository
                .getProductsOrderedByCustomer("mike")
                .doOnNext(product -> log.info("Product : {}",product))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void testGetOrderDetailsByProduct(){
        this.customerOrderRepository
                .getOrderDetailsByProduct("iphone 20")
                .doOnNext(orderDetail -> log.info("OrderDetail: {}",orderDetail))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }



}

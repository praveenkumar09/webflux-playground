package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec04HeaderTest extends AbstractWebClient{

    @Test
    public void test_getProductById_defaultHeader() throws InterruptedException {
        this.createWebClient(b -> b
                .defaultHeader("caller-id", "order-service"))
                .get()
                .uri("/lec04/product/{id}",100)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}

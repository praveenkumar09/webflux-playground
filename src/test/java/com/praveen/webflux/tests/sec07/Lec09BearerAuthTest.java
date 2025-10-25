package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class Lec09BearerAuthTest extends AbstractWebClient {

    @Test
    public void test_getProductById_bearerAuthClient() throws InterruptedException {
        this.createWebClient(b -> b
                        .defaultHeaders(h -> h
                                .setBearerAuth("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")))
                .get()
                .uri("/lec08/product/{id}",100)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}

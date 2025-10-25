package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class Lec08BasicAuthTest extends AbstractWebClient {

    @Test
    public void test_getProductById_basicAuthClient() throws InterruptedException {
        this.createWebClient(b -> b
                        .defaultHeaders(h -> h
                                .setBasicAuth("java", "secret")))
                .get()
                .uri("/lec07/product/{id}",100)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}

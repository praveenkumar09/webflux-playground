package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;

public class Lec04HeaderTest extends AbstractWebClient{

    @Test
    public void test_getProductById_defaultHeader() throws InterruptedException {
        this.createWebClient(b -> b
                .defaultHeader("caller-id",
                        "order-service"))
                .get()
                .uri("/lec04/product/{id}",100)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void test_getProductById_Header() throws InterruptedException {
        this.createWebClient()
                .get()
                .uri("/lec04/product/{id}",100)
                .header("caller-id","order-service")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void test_getProductById_HeaderAsMap() throws InterruptedException {
        Map map = Map.of("caller-id","order-service",
                "timeout", Duration.ofSeconds(3));
        this.createWebClient()
                .get()
                .uri("/lec04/product/{id}",100)
                .headers(h -> h.setAll(map))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}

package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec03PostTest extends AbstractWebClient{

    @Test
    public void test_postProduct(){
        this.createWebClient()
                .post()
                .uri("/lec03/product")
                .bodyValue(new Product(null,"MacBook Pro",1000))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void test_postProductAsBody(){
        Mono<Product> productMono = Mono.
                fromSupplier(() -> new Product(null,"MacBook Pro",1000))
                .delayElement(Duration.ofSeconds(2));
        this.createWebClient()
                .post()
                .uri("/lec03/product")
                .body(productMono,Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}

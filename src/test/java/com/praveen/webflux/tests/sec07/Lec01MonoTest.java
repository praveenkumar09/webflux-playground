package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Lec01MonoTest extends AbstractWebClient{
    private final Logger log = LoggerFactory.getLogger(Lec01MonoTest.class);


    @Test
    public void test_getProductById() throws InterruptedException {
        this.createWebClient()
                .get()
                .uri("/lec01/product/100")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(logResponse())
                .subscribe();
        Thread.sleep(Duration.ofSeconds(3));
    }
}

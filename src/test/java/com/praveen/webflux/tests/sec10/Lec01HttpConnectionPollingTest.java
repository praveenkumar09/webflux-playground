package com.praveen.webflux.tests.sec10;

import com.praveen.webflux.tests.sec10.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lec01HttpConnectionPollingTest extends AbstractWebClient {

    private final WebClient webClient = createWebClient(b -> {
        var poolSize = 1000;
        var provider = ConnectionProvider.builder("webclient-pool")
                .lifo()
                .maxConnections(poolSize)
                .pendingAcquireMaxCount(poolSize * 5)
                .build();
        var httpclient = HttpClient
                .create(provider)
                .keepAlive(true)
                .compress(true);
        b.clientConnector(new ReactorClientHttpConnector(httpclient));
    });


    public Mono<Product> getProductById(int productId){
        return this.webClient
                .get()
                .uri("/product/{productId}",productId)
                .retrieve()
                .bodyToMono(Product.class);
    }

    @Test
    public void test_getProductById_concurrentRequest() throws InterruptedException {
        var max = 1000;
        Flux.range(1,max)
                .flatMap(this::getProductById,
                        max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(products -> {
                    System.out.println("Products: "+products);
                    Assertions.assertEquals(max,products.size());
                })
                .verifyComplete();
    }
}

package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

public class Lec10ExchangeFilterTest extends AbstractWebClient {
    private static final Logger log = LoggerFactory.getLogger(Lec10ExchangeFilterTest.class);


    @Test
    public void test_getProductById_exchangeFilter() throws InterruptedException {
        this.createWebClient(b -> b
                        .filter(this.tokenGenerator()))
                .get()
                .uri("/lec09/product/{id}",100)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    private ExchangeFilterFunction tokenGenerator() {
        return (request, next) -> {
            log.info("Request: {}",request);
            var token= UUID
                    .randomUUID()
                    .toString()
                    .replace("-","");
            ClientRequest modifiedRequest = ClientRequest
                    .from(request)
                    .headers(h -> h
                            .setBearerAuth(token))
                    .build();
            return next.exchange(modifiedRequest);
        };
    }
}

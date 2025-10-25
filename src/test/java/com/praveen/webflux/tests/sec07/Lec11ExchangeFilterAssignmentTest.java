package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.UUID;

public class Lec11ExchangeFilterAssignmentTest extends AbstractWebClient {
    private static final Logger log = LoggerFactory
            .getLogger(Lec11ExchangeFilterAssignmentTest.class);
    private final WebClient client = createWebClient(b ->
            b.filter(this.tokenGenerator()));


    @Test
    public void test_getProductById_exchangeFilter() throws InterruptedException {
        for(int i=1;i<= 5;i++){
            this.client
                    .get()
                    .uri("/lec09/product/{id}",i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(logResponse())
                    .then()
                    .as(StepVerifier::create)
                    .verifyComplete();
        }
    }

    private ExchangeFilterFunction tokenGenerator() {
        return (request, next) -> {
            log.info("Request Method: {}",request
                    .method());
            log.info("Request URI: {}",request
                    .url());
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

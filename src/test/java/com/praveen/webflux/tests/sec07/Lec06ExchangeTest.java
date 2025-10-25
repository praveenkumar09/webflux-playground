package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.CalculatorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

public class Lec06ExchangeTest extends AbstractWebClient{

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(Lec06ExchangeTest.class);

    @Test
    public void test_getCalculation_error() throws InterruptedException {
        this.createWebClient()
                .get()
                .uri("/lec05/calculator/{first}/{second}",100,10)
                .header("operation","+")
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().is4xxClientError()){
                        return clientResponse.bodyToMono(ProblemDetail.class);
                    }
                    log.info("status code : {}",clientResponse.statusCode());
                    clientResponse.headers().asHttpHeaders().forEach((k,v) -> {
                        log.info("{} : {}",k,v);
                    });
                    return clientResponse.bodyToMono(CalculatorResponse.class);
                })
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}

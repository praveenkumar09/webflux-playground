package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.CalculatorResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

public class Lec05ErrorResponseTest extends AbstractWebClient {
    private static final Logger logger = LoggerFactory.getLogger(Lec05ErrorResponseTest.class);


    @Test
    public void test_getCalculation_error() throws InterruptedException {
        this.createWebClient()
                .get()
                .uri("/lec05/calculator/{first}/{second}",100,10)
                .header("operation","0")
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                //.onErrorReturn(new CalculatorResponse(0,0,null,0.0))
                //.onErrorReturn(WebClientResponseException.BadRequest.class,
                        //new CalculatorResponse(0,0,null,0.0))
                .doOnError(WebClientResponseException.class,ex -> {
                    logger.info("Error : {}",ex.getResponseBodyAs(ProblemDetail.class));
                } )
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }
}

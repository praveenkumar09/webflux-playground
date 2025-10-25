package com.praveen.webflux.tests.sec07;

import com.praveen.webflux.tests.sec07.dto.CalculatorResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import reactor.test.StepVerifier;

import java.util.Map;

public class Lec07QueryParamsTest extends AbstractWebClient {
    private static final Logger logger = LoggerFactory.getLogger(Lec07QueryParamsTest.class);


    @Test
    public void test_getCalculation_queryParams() throws InterruptedException {
        String path = "/lec06/calculator";
        String queryParam = "first={first}&second={second}&operation={operation}";
        this.createWebClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path(path).query(queryParam).build("100", "10", "+"))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void test_getCalculation_queryParamsAsMap() throws InterruptedException {
        String path = "/lec06/calculator";
        String queryParam = "first={first}&second={second}&operation={operation}";
        var map = Map.of("first","100",
                "second","10",
                "operation","+");
        this.createWebClient()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .query(queryParam)
                        .build(map))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(logResponse())
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

}

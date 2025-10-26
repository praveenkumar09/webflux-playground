package com.praveen.webflux.tests.sec09;

import com.praveen.webflux.sec09.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@SpringBootTest(properties = {
        "sec=sec09"
})
@AutoConfigureWebTestClient
public class ServerSentEventsTest {

    private static final Logger log = LoggerFactory.getLogger(ServerSentEventsTest.class);

    @Autowired
    private WebTestClient webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:8080")
            .build();

    @Test
    public void serverSentEventsTest(){
        this.webTestClient
                .get()
                .uri("/products/stream/80")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(ProductDto.class)
                .getResponseBody()
                .take(10)
                .doOnNext(product -> log.info("{}", product))
                .collectList()
                .as(StepVerifier::create)
                .assertNext(products -> {
                    log.info("{}", products);
                    assert products.size() == 10;
                })
                .verifyComplete();
    }


}

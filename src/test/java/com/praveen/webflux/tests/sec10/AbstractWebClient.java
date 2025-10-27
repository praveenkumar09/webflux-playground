package com.praveen.webflux.tests.sec10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

abstract class AbstractWebClient {
    private final Logger log = LoggerFactory.getLogger(AbstractWebClient.class);

    protected <T> Consumer<T> logResponse() {
        return response -> log.info("Response: {}", response);
    }

    protected WebClient createWebClient() {
        return createWebClient(builder -> {});
    }

    protected WebClient createWebClient(Consumer<WebClient.Builder> consumer) {
        WebClient.Builder builder = WebClient
                .builder()
                .baseUrl("http://localhost:7070/demo03");
        consumer.accept(builder);
        return builder.build();
    }


}

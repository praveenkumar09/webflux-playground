package com.praveen.webflux.sec05.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class FilterErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<Void> handleError(ServerWebExchange exchange,
                                  HttpStatus status,
                                  String title,
                                  String message){
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(status, message);
        problemDetail
                .setTitle(title);
        problemDetail.setType(URI
                .create("http://example.com/problems/authentication-error"));
        byte[] bytes = null;
        try {
            bytes = objectMapper.writeValueAsBytes(problemDetail);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ServerHttpResponse response = exchange
                .getResponse();
        DataBuffer dataBuffer = response
                .bufferFactory()
                .wrap(bytes);
        return response
                .writeWith(Mono.just(dataBuffer));
    }
}

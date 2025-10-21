package com.praveen.webflux.sec05.filter;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Service
@Order(2)
public class AuthorizationWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Category category = exchange
                .getAttributeOrDefault("category", Category.STANDARD);
        return switch (category){
            case PRIME -> prime(exchange, chain);
            case STANDARD -> standard(exchange, chain);
        };
    }

    public Mono<Void> prime(ServerWebExchange exchange, WebFilterChain chain) {
        return chain
                .filter(exchange);
    }

    public Mono<Void> standard(ServerWebExchange exchange, WebFilterChain chain) {
        if(isGET().test(exchange)){
            return chain
                    .filter(exchange);
        }else{
            String body = "{\"error\": \"Forbidden\", " +
                    "\"message\": \"Forbidden access\"}";
            ServerHttpResponse response = exchange
                    .getResponse();
            response
                    .setStatusCode(HttpStatus.FORBIDDEN);
            response
                    .getHeaders()
                            .setContentType(MediaType.APPLICATION_JSON);
            DataBuffer dataBuffer = response
                    .bufferFactory()
                    .wrap(body.getBytes());
            return response
                    .writeWith(Mono.just(dataBuffer));
        }
    }

    public Predicate<ServerWebExchange> isGET(){
        return (b) -> b
                .getRequest()
                .getMethod()
                .equals(HttpMethod.GET);
    }


}

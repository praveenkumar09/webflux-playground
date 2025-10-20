package com.praveen.webflux.sec05.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Service
public class AuthenticationWebFilter implements WebFilter {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationWebFilter.class);


    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain
    ) {
        log.info("Authentication filter invoked");
        String token = exchange
                .getRequest()
                .getHeaders()
                .getFirst("auth-token");
        Category category = Category.fromToken(token);
        if(category != null) {
            return chain.filter(exchange);
        }else {
            log.info("Authentication failed");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            String body = "{\"error\": \"Unauthorized\", \"message\": \"Invalid or missing auth token\"}";
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes());

            return exchange.getResponse().writeWith(Mono.just(buffer));
        }

    }
}

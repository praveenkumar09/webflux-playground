package com.praveen.webflux.sec05.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Service
@Order(1)
public class AuthenticationWebFilter implements WebFilter {
    private static final Logger log = LoggerFactory
            .getLogger(AuthenticationWebFilter.class);

    private final FilterErrorHandler filterErrorHandler;

    @Autowired
    public AuthenticationWebFilter(FilterErrorHandler filterErrorHandler) {
        this.filterErrorHandler = filterErrorHandler;
    }

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
            log.info("Authentication successful");
            exchange.getAttributes()
                    .put("category", category);
            return chain.filter(exchange);
        }else {
            log.info("Authentication failed");
            return filterErrorHandler
                    .handleError(
                            exchange,
                            HttpStatus.UNAUTHORIZED,
                            "Unauthorized",
                            "Auth token is in-valid");
        }

    }
}

package com.praveen.webflux.webflux_playground.sec01;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("/traditional")
public class TraditionalWebController {
    private static final Logger log = LoggerFactory.getLogger(TraditionalWebController.class);
    private final RestClient restClient = RestClient
            .builder()
            .requestFactory(new JdkClientHttpRequestFactory())
            .baseUrl("http://localhost:7070")
            .build();


    @GetMapping("/products")
    public List<Product> getProducts() {
        List<Product> products = this.restClient
                .get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });
        log.info("Products: {}", products);
        return products;
    }

    @Deprecated
    @GetMapping("/notreactive/products")
    public Flux<Product> getProductsNotReactive(){
        List<Product> products = this.restClient
                .get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });
        log.info("Products: {}", products);
        return Flux.fromIterable(products);
    }
}

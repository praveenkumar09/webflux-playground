package com.praveen.webflux.tests.sec08;

import com.praveen.webflux.sec08.dto.ProductDto;
import com.praveen.webflux.sec08.dto.ProductUploadResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductClient {

    private final WebClient webClient = WebClient
            .builder()
            .baseUrl("http://localhost:8080/api/v1/products")
            .build();

    public Mono<ProductUploadResponse> uploadProduct(Flux<ProductDto> product){
        return this.webClient
                .post()
                .uri("/upload")
                .contentType(MediaType.APPLICATION_NDJSON)
                .body(product, ProductDto.class)
                .retrieve()
                .bodyToMono(ProductUploadResponse.class);
    }

}

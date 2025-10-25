package com.praveen.webflux.sec08.controller;

import com.praveen.webflux.sec08.dto.ProductDto;
import com.praveen.webflux.sec08.dto.ProductUploadResponse;
import com.praveen.webflux.sec08.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "upload",
    consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<ProductUploadResponse> uploadProducts(
            @RequestBody Flux<ProductDto> productDtoFlux
    ){
        log.info("Received request to upload products");
        return this.productService
                .saveProducts(productDtoFlux)
                .then(this.productService
                        .countProducts())
                .map(count -> new ProductUploadResponse(
                        UUID.randomUUID(),
                        count
                ));
    }

}

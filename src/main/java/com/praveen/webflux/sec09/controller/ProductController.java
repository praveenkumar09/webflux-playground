package com.praveen.webflux.sec09.controller;

import com.praveen.webflux.sec09.dto.ProductDto;
import com.praveen.webflux.sec09.dto.ProductUploadResponse;
import com.praveen.webflux.sec09.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Mono<ProductDto> uploadProducts(
            @RequestBody Mono<ProductDto> productDtoMono
    ){
        log.info("Received request to upload products");
        return this.productService
                .saveProducts(productDtoMono);
    }

    @GetMapping(value = "/stream/{maxPrice}",
    produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDto> productStream(
            @PathVariable Integer maxPrice
    ){
        return this.productService
                .productStream()
                .filter(dto -> dto.price() <= maxPrice);
    }

}

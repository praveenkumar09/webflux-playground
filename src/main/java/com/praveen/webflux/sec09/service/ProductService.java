package com.praveen.webflux.sec09.service;

import com.praveen.webflux.sec09.dto.ProductDto;
import com.praveen.webflux.sec09.mapper.ProductDtoMapper;
import com.praveen.webflux.sec09.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final Sinks.Many<ProductDto> productSink;

    @Autowired
    public ProductService(
            ProductRepository productRepository,
            Sinks.Many<ProductDto> productSink
    ){
        this.productRepository = productRepository;
        this.productSink = productSink;
    }

    public Mono<ProductDto> saveProducts(
            Mono<ProductDto> productDtoMono
    ){
        return productDtoMono
                .map(ProductDtoMapper::toEntity)
                .flatMap(this
                        .productRepository::save)
                .map(ProductDtoMapper::toDto)
                .doOnNext(productSink::tryEmitNext);
    }

    public Flux<ProductDto> productStream(){
        return this.productSink
                .asFlux();
    }


}

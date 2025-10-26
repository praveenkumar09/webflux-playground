package com.praveen.webflux.sec08.service;

import com.praveen.webflux.sec08.dto.ProductDto;
import com.praveen.webflux.sec08.mapper.ProductDtoMapper;
import com.praveen.webflux.sec08.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(
            ProductRepository productRepository
    ){
        this.productRepository = productRepository;
    }

    public Flux<ProductDto> saveProducts(
            Flux<ProductDto> productDtoFlux
    ){
        return productDtoFlux
                .map(ProductDtoMapper::toEntity)
                .as(this.productRepository::saveAll)
                .map(ProductDtoMapper::toDto);
    }

    public Mono<Long> countProducts(){
        return this.productRepository.count();
    }

    public Flux<ProductDto> getAllProducts(){
        return this.productRepository.findAll()
                .map(ProductDtoMapper::toDto);
    }


}

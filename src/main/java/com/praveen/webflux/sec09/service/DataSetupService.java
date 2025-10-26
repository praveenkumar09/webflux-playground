package com.praveen.webflux.sec09.service;

import com.praveen.webflux.sec09.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataSetupService implements CommandLineRunner {

    private final ProductService productService;


    @Autowired
    public DataSetupService(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        Flux.range(1, 1000)
                .delayElements(Duration.ofSeconds(1))
                .map(i ->
                        new ProductDto(null,
                                "Product "+i,
                                ThreadLocalRandom
                        .current().nextInt(1,100)))
                .flatMap(productDto -> this
                        .productService
                        .saveProducts(Mono.just(productDto)))
                .subscribe();
    }
}

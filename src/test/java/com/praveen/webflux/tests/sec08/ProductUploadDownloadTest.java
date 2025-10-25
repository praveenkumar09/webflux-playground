package com.praveen.webflux.tests.sec08;

import com.praveen.webflux.sec08.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class ProductUploadDownloadTest {

    private static final Logger log = LoggerFactory.getLogger(ProductUploadDownloadTest.class);
    private final ProductClient productClient = new ProductClient();
    @Test
    public void test_upload(){
        Flux<ProductDto> productFlux = Flux
                .range(1,1_000_000)
                .map(i ->
                        new ProductDto(null,"Product "+i,i*1000)
                );
        this.productClient
                .uploadProduct(productFlux)
                .doOnNext(r -> log.info("Response: {}",r))
                .then()
                .as(StepVerifier::create)
                .verifyComplete();
    }

}

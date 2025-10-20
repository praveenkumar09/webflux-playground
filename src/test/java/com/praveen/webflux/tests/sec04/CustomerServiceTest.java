package com.praveen.webflux.tests.sec04;

import com.praveen.webflux.sec04.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec04")
public class CustomerServiceTest {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void test_getAllCustomers(){
        this.webTestClient
                .get()
                .uri("/customers/all")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM_VALUE)
                .expectBodyList(CustomerDto.class)
                .hasSize(10);
    }

    @Test
    public void test_getAllCustomersPaginated(){
        this.webTestClient
                .get()
                .uri("/customers/all/paginated?pageNo=1&pageSize=3")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM_VALUE)
                .returnResult(CustomerDto.class)
                .getResponseBody()
                .collectList()
                .as(StepVerifier::create)
                .assertNext(customers -> {
                    assertThat(customers).hasSize(3);
                    assertThat(customers.get(0).id()).isEqualTo(1);
                    assertThat(customers.get(1).id()).isEqualTo(2);
                    assertThat(customers.get(2).id()).isEqualTo(3);
                })
                .verifyComplete();

    }

    @Test
    public void test_getCustomerById(){
        this.webTestClient
                .get()
                .uri("/customers/1")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(CustomerDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(customer -> {
                    assertThat(customer.id()).isEqualTo(1);
                    assertThat(customer.name()).isEqualTo("sam");
                })
                .verifyComplete();
    }

    @Test
    public void test_saveCustomer(){
        var dto = new CustomerDto(null,"ethan","ethan@gmail.com");
        this.webTestClient
                .post()
                .uri("/customers/save")
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(CustomerDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(savedCustomer -> {
                    assertThat(savedCustomer.id()).isNotNull();
                    assertThat(savedCustomer.name()).isEqualTo("ethan");
                    assertThat(savedCustomer.email()).isEqualTo("ethan@gmail.com");
                })
                .verifyComplete();

    }

    @Test
    public void test_updateCustomer(){
        CustomerDto dto = new CustomerDto(null,"ethan","ethan@gmail.com");
        this.webTestClient
                .put()
                .uri("/customers/update/10")
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(CustomerDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .assertNext(updatedCustomer -> {
                    assertThat(updatedCustomer.id()).isEqualTo(10);
                    assertThat(updatedCustomer.name()).isEqualTo("ethan");
                    assertThat(updatedCustomer.email()).isEqualTo("ethan@gmail.com");
                })
                .verifyComplete();
    }

    @Test
    public void test_deleteCustomer(){
        this.webTestClient
                .delete()
                .uri("/customers/delete/10")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void test_getCustomer_notFound(){
        this.webTestClient
                .get()
                .uri("/customers/12")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody()
                .consumeWith(r -> log
                        .info("{}",r.getResponseBody()))
                .jsonPath("$.detail")
                .isEqualTo("Customer [id=12] is not found");
    }

    @Test
    public void test_saveCustomer_nameEmpty(){
        var dto = new CustomerDto(null,"","ethan@gmail.com");
        this.webTestClient
                .post()
                .uri("/customers/save")
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .consumeWith(r -> {
                    log.info("Problem : {}",new String(r.getResponseBody()));
                })
                .jsonPath("$.detail")
                .isEqualTo("Name is required");

    }

    @Test
    public void test_saveCustomer_emailEmpty(){
        var dto = new CustomerDto(null,"ethan","gmail.com");
        this.webTestClient
                .post()
                .uri("/customers/save")
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .consumeWith(r -> {
                    log.info("Problem : {}",new String(r.getResponseBody()));
                })
                .jsonPath("$.detail")
                .isEqualTo("Email is required");

    }

}

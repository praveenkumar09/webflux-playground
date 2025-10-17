package com.praveen.webflux.tests.sec02;

import com.praveen.webflux.sec02.entity.Customer;
import com.praveen.webflux.sec02.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.Objects;

public class Lec01CustomerRepositoryTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(Lec01CustomerRepositoryTest.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void _test_FindAll() {
        this.customerRepository.findAll()
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void _test_findById(){
        this.customerRepository.findById(1)
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .expectNextMatches(customer -> Objects.equals(customer.getName(), "sam"))
                .verifyComplete();
    }

    @Test
    public void _test_findByName(){
       this.customerRepository.findByName("sam")
               .doOnNext(customer -> log.info("Customer: {}", customer))
               .as(StepVerifier::create)
               .assertNext(customer ->
                       Assertions
                               .assertEquals(
                                       "sam@gmail.com",
                       customer.getEmail()
                               )
               )
               .verifyComplete();
    }

    @Test
    public void _test_findByEmail(){
        this.customerRepository.findByEmail("sam@gmail.com")
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .assertNext(customer ->
                        Assertions
                                .assertEquals(
                                        "sam",
                                        customer.getName()
                                )
                )
                .verifyComplete();
    }

    @Test
    public void _test_findByNameAndEmail(){
        this.customerRepository.findByNameAndEmail("sam","sam@gmail.com")
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .assertNext(customer ->
                        Assertions
                                .assertEquals(
                                        1,
                                        customer.getId()
                                )
                )
                .verifyComplete();
    }

    @Test
    public void _test_findByEmailEndingWith(){
        this.customerRepository.findByEmailEndingWith("ke@gmail.com")
                .doOnNext(customer -> log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void _test_insertAndDeleteCustomer(){
        Customer customer = new Customer();
        customer.setName("test");
        customer.setEmail("test@gmail.com");
        this.customerRepository.save(customer)
                .as(StepVerifier::create)
                .assertNext(savedCustomer -> {
                    Assertions.assertNotNull(savedCustomer.getId());
                })
                .verifyComplete();

        this.customerRepository.count()
                .as(StepVerifier::create)
                .expectNext(11L)
                .verifyComplete();

        this.customerRepository.deleteById(customer.getId())
                .then(this.customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .verifyComplete();
    }

    @Test
    public void _test_updateCustomer(){
        this.customerRepository.findByName("ethan")
                .doOnNext(customer -> {
                    customer.setName("noel");
                    customer.setEmail("noel@gmail.com");
                })
                .flatMap(customer -> this.customerRepository.save(customer))
                .doOnNext(customer ->
                        log.info("Customer: {}", customer))
                .as(StepVerifier::create)
                .assertNext(customer -> Assertions.assertNotNull(customer.getId()))
                .verifyComplete();

    }

}

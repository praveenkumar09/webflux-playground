package com.praveen.webflux.tests.sec02;

import com.praveen.webflux.sec02.dto.OrderDetails;
import com.praveen.webflux.sec02.repository.CustomerOrderRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

public class Lec04DatabaseClientTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(Lec04DatabaseClientTest.class);


    @Autowired
    private DatabaseClient databaseClient;

    @Test
    public void testGetOrderDetailsByProduct(){
        String query = """
                SELECT
                co.order_id,
                c.name AS customer_name,
                p.description AS product_name,
                co.amount,
                co.order_date
            FROM
                customer c
            INNER JOIN customer_order co ON c.id = co.customer_id
            INNER JOIN product p ON p.id = co.product_id
            WHERE
                p.description = :description
            ORDER BY co.amount DESC
            """;

        this.databaseClient
                .sql(query)
                .bind("description", "iphone 20")
                .mapProperties(OrderDetails.class)
                .all()
                .doOnNext(orderDetail -> log.info("OrderDetail: {}",orderDetail))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }
}

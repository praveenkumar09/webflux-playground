package com.praveen.webflux.sec02.dto;

import java.time.Instant;

public record OrderDetails(Integer orderId,
                           String customerName,
                           String productName,
                           Integer amount,
                           Instant orderDate) {
}

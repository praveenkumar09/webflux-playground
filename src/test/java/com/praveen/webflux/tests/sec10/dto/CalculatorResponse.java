package com.praveen.webflux.tests.sec10.dto;

public record CalculatorResponse(Integer first,
                                 Integer second,
                                 String operation,
                                 Double result) {
}

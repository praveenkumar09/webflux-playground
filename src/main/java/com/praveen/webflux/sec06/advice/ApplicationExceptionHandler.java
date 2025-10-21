package com.praveen.webflux.sec06.advice;

import com.praveen.webflux.sec06.exceptions.CustomerNotFoundException;
import com.praveen.webflux.sec06.exceptions.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

@Service
public class ApplicationExceptionHandler {

    public Mono<ServerResponse> handleException(CustomerNotFoundException ex, ServerRequest request){
        return handleException(HttpStatus.NOT_FOUND, ex, request, problemDetail -> {
            problemDetail.setType(URI.create("http://example.com/problems/customer-not-found"));
            problemDetail.setTitle("Customer Not Found");
        });
    }

    public Mono<ServerResponse> handleException(InvalidInputException ex, ServerRequest request){
        return handleException(HttpStatus.BAD_REQUEST, ex, request, problemDetail -> {
            problemDetail.setType(URI
                    .create("http://example.com/problems/invalid-input"));
            problemDetail.setTitle("Invalid Input");
        });
    }

    private Mono<ServerResponse> handleException(HttpStatus status, Exception ex, ServerRequest request, Consumer<ProblemDetail> consumer){
        ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(status,
                        ex.getMessage());
        problemDetail.setInstance(URI.create(request.path()));
        consumer.accept(problemDetail);
        return ServerResponse.status(status).bodyValue(problemDetail);
    }
}

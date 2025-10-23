package com.praveen.webflux.sec00.error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

@Component
public class ErrorHandlerFunction {

    public Mono<ServerResponse> handleBadRequestError(Throwable ex, ServerRequest req) {
        return handleException(HttpStatus.BAD_REQUEST,ex,req, problemDetail -> {
            problemDetail.setType(URI.create("http://example.com/problems/bad-request"));
            problemDetail.setDetail("Invalid input");
        });
    }

    public Mono<ServerResponse> handleServerError(Throwable ex, ServerRequest req) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR,ex,req, problemDetail -> {
            problemDetail.setType(URI.create("http://example.com/problems/internal-server-error"));
            problemDetail.setDetail(ex.getMessage());
        });
    }

    public Mono<ServerResponse> handleException(HttpStatus status, Throwable ex, ServerRequest request, Consumer<ProblemDetail> consumer){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle(ex.getClass().getSimpleName());
        problemDetail.setInstance(URI.create(request.path()));
        consumer.accept(problemDetail);
        return ServerResponse.status(status).bodyValue(problemDetail);
    }
}

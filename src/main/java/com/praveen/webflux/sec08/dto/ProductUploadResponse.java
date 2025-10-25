package com.praveen.webflux.sec08.dto;

import java.util.UUID;

public record ProductUploadResponse(UUID confirmationId,
                                    Long productsCount) {
}

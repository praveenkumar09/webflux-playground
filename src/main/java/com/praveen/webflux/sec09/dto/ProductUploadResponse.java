package com.praveen.webflux.sec09.dto;

import java.util.UUID;

public record ProductUploadResponse(UUID confirmationId,
                                    Long productsCount) {
}

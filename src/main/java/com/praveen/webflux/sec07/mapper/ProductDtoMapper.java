package com.praveen.webflux.sec07.mapper;

import com.praveen.webflux.sec07.dto.ProductDto;
import com.praveen.webflux.sec07.entity.Product;

public class ProductDtoMapper {

    public static Product toEntity(ProductDto dto) {
        return new Product(dto.id(),
                dto.description(),
                dto.price());
    }

    public static ProductDto toDto(Product entity) {
        return new ProductDto(entity.getId(), entity.getDescription(), entity.getPrice());
    }
}

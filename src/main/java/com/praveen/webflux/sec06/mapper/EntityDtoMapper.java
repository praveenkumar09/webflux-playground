package com.praveen.webflux.sec06.mapper;

import com.praveen.webflux.sec06.dto.CustomerDto;
import com.praveen.webflux.sec06.entity.Customer;

public class EntityDtoMapper {


    public static Customer toEntity(CustomerDto dto) {
        return new Customer(dto.id(),
                dto.name(),
                dto.email());
    }

    public static CustomerDto toDto(Customer entity) {
        return new CustomerDto(entity.getId(), entity.getName(), entity.getEmail());
    }
}

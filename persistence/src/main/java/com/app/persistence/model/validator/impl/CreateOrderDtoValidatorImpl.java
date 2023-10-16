package com.app.persistence.model.validator.impl;

import com.app.persistence.model.dto.order.CreateOrderDto;

import com.app.persistence.model.validator.CreateOrderDtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.app.persistence.data.reader.validator.DataValidator.validateIntLowerThan;
import static com.app.persistence.data.reader.validator.DataValidator.validateMatchesRegex;

import static com.app.persistence.model.validator.generic.DtoValidator.validateNull;
import static java.lang.Integer.parseInt;

@Component
@RequiredArgsConstructor
public class CreateOrderDtoValidatorImpl implements CreateOrderDtoValidator {
    @Value("${minQuantity}")
    private String minQuantity;
    @Value("${nameRegex}")
    private String nameRegex;
    @Value("${surnameRegex}")
    private String surnameRegex;
    @Value("${emailRegex}")
    private String emailRegex;
    @Value("${minAge}")
    private String minAge;

    @Override
    public void validate(CreateOrderDto createOrderDto) {
        validateNull(createOrderDto, "CreateOrderDto is null");

        validateIntLowerThan(createOrderDto.quantity(), parseInt(minQuantity));
        validateMatchesRegex(nameRegex, createOrderDto.customerName());
        validateMatchesRegex(surnameRegex, createOrderDto.surname());
        validateMatchesRegex(emailRegex, createOrderDto.email());
        validateIntLowerThan(createOrderDto.age(), parseInt(minAge));
    }
}

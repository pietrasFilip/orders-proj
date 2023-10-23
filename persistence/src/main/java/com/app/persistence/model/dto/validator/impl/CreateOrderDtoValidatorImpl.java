package com.app.persistence.model.dto.validator.impl;

import com.app.persistence.model.dto.order.CreateOrderDto;

import com.app.persistence.model.dto.validator.CreateOrderDtoValidator;
import com.app.persistence.model.dto.validator.generic.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.app.persistence.data.reader.validator.DataValidator.validateIntLowerThan;
import static com.app.persistence.data.reader.validator.DataValidator.validateMatchesRegex;

import static java.lang.Integer.parseInt;

@Component
@RequiredArgsConstructor
public class CreateOrderDtoValidatorImpl implements CreateOrderDtoValidator {
    @Value("${validator.min.quantity}")
    private String minQuantity;
    @Value("${validator.regex.name}")
    private String nameRegex;
    @Value("${validator.regex.surname}")
    private String surnameRegex;
    @Value("${validator.regex.email}")
    private String emailRegex;
    @Value("${validator.min.age}")
    private String minAge;

    @Override
    public void validate(CreateOrderDto createOrderDto) {
        DtoValidator.validateNull(createOrderDto, "CreateOrderDto is null");

        validateIntLowerThan(createOrderDto.quantity(), parseInt(minQuantity));
        validateMatchesRegex(nameRegex, createOrderDto.customerName());
        validateMatchesRegex(surnameRegex, createOrderDto.surname());
        validateMatchesRegex(emailRegex, createOrderDto.email());
        validateIntLowerThan(createOrderDto.age(), parseInt(minAge));
    }
}

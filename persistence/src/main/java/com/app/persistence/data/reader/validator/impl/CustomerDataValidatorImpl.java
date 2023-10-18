package com.app.persistence.data.reader.validator.impl;

import com.app.persistence.data.reader.model.CustomerData;
import com.app.persistence.data.reader.validator.CustomerDataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.app.persistence.data.reader.validator.DataValidator.validateIntLowerThan;
import static com.app.persistence.data.reader.validator.DataValidator.validateMatchesRegex;

@Component
@RequiredArgsConstructor
public class CustomerDataValidatorImpl implements CustomerDataValidator {
    @Value("${validator.regex.name}")
    private String nameRegex;

    @Value("${validator.regex.surname}")
    private String surnameRegex;

    @Value("${validator.min.age}")
    private int minAge;

    @Value("${validator.regex.email}")
    private String emailRegex;

    @Override
    public CustomerData validate(CustomerData customerData) {
        return CustomerData.of(
                customerData.getCustomerId(),
                validateMatchesRegex(nameRegex, customerData.getName()),
                validateMatchesRegex(surnameRegex, customerData.getSurname()),
                validateIntLowerThan(customerData.getAge(), minAge),
                validateMatchesRegex(emailRegex, customerData.getEmail())
        );
    }
}

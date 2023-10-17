package com.app.persistence.model.dto.validator.impl;

import com.app.persistence.model.dto.user.CreateUserDto;
import com.app.persistence.model.dto.validator.CreateUserDtoValidator;
import org.springframework.stereotype.Component;

import static com.app.persistence.model.dto.validator.generic.DtoValidator.validateNull;

@Component
public class CreateUserDtoValidatorImpl implements CreateUserDtoValidator {
    @Override
    public void validate(CreateUserDto createUserDto) {
        validateNull(createUserDto.username(), "Username is null or empty");
        validateNull(createUserDto.email(), "Email is null or empty");
        validateNull(createUserDto.password(), "Password is null or empty");
        validateNull(createUserDto.passwordConfirmation(), "Password confirmation is null or empty");
    }
}

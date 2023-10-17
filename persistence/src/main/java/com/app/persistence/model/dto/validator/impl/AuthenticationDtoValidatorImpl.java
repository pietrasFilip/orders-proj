package com.app.persistence.model.dto.validator.impl;

import com.app.persistence.model.dto.token.AuthenticationDto;
import com.app.persistence.model.dto.validator.AuthenticationDtoValidator;
import org.springframework.stereotype.Component;

import static com.app.persistence.model.dto.validator.generic.DtoValidator.validateNull;


@Component
public class AuthenticationDtoValidatorImpl implements AuthenticationDtoValidator {
    @Override
    public void validate(AuthenticationDto authenticationDto) {
        validateNull(authenticationDto.username(), "AuthenticationDto - username is null");
        validateNull(authenticationDto.password(), "Password is null");
    }
}

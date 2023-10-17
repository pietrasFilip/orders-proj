package com.app.persistence.model.dto.validator.impl;

import com.app.persistence.model.dto.token.AuthorizationDto;
import com.app.persistence.model.dto.validator.AuthorizationDtoValidator;
import org.springframework.stereotype.Component;

import static com.app.persistence.model.dto.validator.generic.DtoValidator.validateNull;


@Component
public class AuthorizationDtoValidatorImpl implements AuthorizationDtoValidator {
    @Override
    public void validate(AuthorizationDto authorizationDto) {
        validateNull(authorizationDto.role(),"AuthenticationDto - username is null");
    }
}

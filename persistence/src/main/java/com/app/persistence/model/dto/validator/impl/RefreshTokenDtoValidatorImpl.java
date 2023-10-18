package com.app.persistence.model.dto.validator.impl;

import com.app.persistence.model.dto.token.RefreshTokenDto;
import com.app.persistence.model.dto.validator.RefreshTokenDtoValidator;
import org.springframework.stereotype.Component;

import static com.app.persistence.model.dto.validator.generic.DtoValidator.validateNull;


@Component
public class RefreshTokenDtoValidatorImpl implements RefreshTokenDtoValidator {
    @Override
    public void validate(RefreshTokenDto refreshTokenDto) {
        validateNull(refreshTokenDto.token(), "RefreshTokenDto - token is null");
    }
}

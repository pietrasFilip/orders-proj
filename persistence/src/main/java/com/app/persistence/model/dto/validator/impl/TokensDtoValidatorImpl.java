package com.app.persistence.model.dto.validator.impl;

import com.app.persistence.model.dto.token.TokensDto;
import com.app.persistence.model.dto.validator.TokensDtoValidator;
import org.springframework.stereotype.Component;

import static com.app.persistence.model.dto.validator.generic.DtoValidator.validateNull;


@Component
public class TokensDtoValidatorImpl implements TokensDtoValidator {
    @Override
    public void validate(TokensDto tokensDto) {
        validateNull(tokensDto.access(), "TokensDto - access is null");
        validateNull(tokensDto.refresh(), "TokensDto - refresh is null");
    }
}

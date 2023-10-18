package com.app.service.token;

import com.app.persistence.model.dto.token.AuthorizationDto;
import com.app.persistence.model.dto.token.RefreshTokenDto;
import com.app.persistence.model.dto.token.TokensDto;

public sealed interface TokensService permits TokensServiceImpl {
    TokensDto generateTokens(Long userId);
    AuthorizationDto parseTokens(String token);
    TokensDto refreshTokens(RefreshTokenDto refreshTokenDto);
}

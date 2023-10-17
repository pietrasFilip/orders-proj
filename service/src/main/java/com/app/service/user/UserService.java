package com.app.service.user;

import com.app.persistence.model.dto.token.AuthenticationDto;
import com.app.persistence.model.dto.user.CreateUserDto;
import com.app.persistence.model.dto.user.GetUserDto;

public interface UserService {
    GetUserDto register(CreateUserDto createUserDto);
    GetUserDto activate(Long userId, Long expirationTime);
    GetUserDto findById(Long id);
    GetUserDto findByUsername(String username);
    GetUserDto findByEmail(String email);
    Long isUserCorrect(AuthenticationDto authenticationDto);
}

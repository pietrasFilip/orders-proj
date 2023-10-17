package com.app.persistence.model.user;

import com.app.persistence.model.dto.token.AuthorizationDto;
import com.app.persistence.model.dto.user.GetUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private int isActive;

    public User withPassword(String password) {
        return User
                .builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .isActive(isActive)
                .build();
    }

    public User withActiveState(int isActive) {
        return User
                .builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .isActive(isActive)
                .build();
    }

    public GetUserDto toGetUserDto() {
        return new GetUserDto(id, username, email);
    }

    public AuthorizationDto toAuthorizationDto() {
        return new AuthorizationDto(role);
    }

}

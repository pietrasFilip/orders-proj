package com.app.persistence.model.dto.validator.token;

import com.app.persistence.model.user.Role;

import static com.app.persistence.model.user.Role.ADMIN;
import static com.app.persistence.model.user.Role.USER;

public record AuthorizationDto(Role role) {
    public boolean isAuth() {
        return role != null;
    }

    public boolean isUser() {
        return role.equals(USER);
    }

    public boolean isAdmin() {
        return role.equals(ADMIN);
    }
}

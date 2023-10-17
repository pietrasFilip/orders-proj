package com.app.service.user;

import com.app.persistence.data.reader.loader.repository.db.UserRepository;
import com.app.persistence.model.dto.user.GetUserDto;
import com.app.persistence.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivateTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("When userId is null")
    void test1() {
        assertThatThrownBy(() -> userService.activate(null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User id is null");
    }

    @Test
    @DisplayName("When expiration time is null")
    void test2() {
        assertThatThrownBy(() -> userService.activate(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Expiration time is null");
    }

    @Test
    @DisplayName("When token has expired")
    void test3() {
        var expirationTime = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - 10;

        assertThatThrownBy(() -> userService.activate(1L, expirationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Registration token has been expired");
    }

    @Test
    @DisplayName("When User is activated")
    void test4() {
        var expirationTime = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + 1000000;

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(
                        User
                                .builder()
                                .id(1L)
                                .username("user")
                                .email("email")
                                .isActive(0)
                                .build()
                ));

        when(userRepository.update(1L, User
                .builder()
                .id(1L)
                .username("user")
                .email("email")
                .isActive(1)
                .build()
        ))
                .thenReturn(Optional.of(User
                        .builder()
                        .id(1L)
                        .username("user")
                        .email("email")
                        .isActive(1)
                        .build()
                ));

        assertThat(userService.activate(1L, expirationTime))
                .isInstanceOf(GetUserDto.class)
                .isEqualTo(new GetUserDto(1L, "user", "email"));
    }
}

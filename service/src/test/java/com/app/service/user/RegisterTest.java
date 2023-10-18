package com.app.service.user;

import com.app.persistence.data.reader.loader.repository.db.UserRepository;
import com.app.persistence.model.dto.user.CreateUserDto;
import com.app.persistence.model.dto.user.GetUserDto;
import com.app.persistence.model.dto.validator.CreateUserDtoValidator;
import com.app.persistence.model.user.User;
import com.app.service.email.EmailServiceImpl;
import com.app.service.user.exception.UserServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.Stream;

import static com.app.persistence.model.user.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    CreateUserDtoValidator createUserDtoValidator;
    @Mock
    EmailServiceImpl emailService;
    @Mock
    Environment environment;
    @InjectMocks
    UserServiceImpl userService;

    static Stream<Arguments> argSource() {
        return Stream.of(
                Arguments.of(
                        new CreateUserDto("user", "email", "pass", "pass", USER)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("argSource")
    @DisplayName("When user with username already exists in db")
    void test1(CreateUserDto createUserDto) {
        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(User.builder().username("user").build()));


        assertThatThrownBy(() -> userService.register(createUserDto))
                .isInstanceOf(UserServiceException.class)
                .hasMessage("Username already exists");
    }

    @ParameterizedTest
    @MethodSource("argSource")
    @DisplayName("When user with email already exists in db")
    void test2(CreateUserDto createUserDto) {
        when(userRepository.findByEmail("email"))
                .thenReturn(Optional.of(User.builder().email("email").build()));


        assertThatThrownBy(() -> userService.register(createUserDto))
                .isInstanceOf(UserServiceException.class)
                .hasMessage("Email already exists");
    }

    @ParameterizedTest
    @MethodSource("argSource")
    @DisplayName("When activation mail is sent")
    void test3(CreateUserDto createUserDto) {
        var userToRegister = User
                .builder()
                .username("user")
                .email("email")
                .role(USER)
                .isActive(0)
                .build();

        when(userRepository.save(userToRegister))
                .thenReturn(Optional.of(userToRegister));

        when(environment.getRequiredProperty("registration.activation.time"))
                .thenReturn("120000");

        when(environment.getRequiredProperty("registration.email.subject"))
                .thenReturn("Register activation link");

        when(environment.getRequiredProperty("registration.email.content"))
                .thenReturn("Click to activate your account:");

        userService.register(createUserDto);

        verify(emailService, times(1))
                .send(anyString(), anyString(), anyString());
    }

    @ParameterizedTest
    @MethodSource("argSource")
    @DisplayName("When user is registered")
    void test4(CreateUserDto createUserDto) {
        var userToRegister = User
                .builder()
                .username("user")
                .email("email")
                .role(USER)
                .isActive(0)
                .build();

        when(userRepository.save(userToRegister))
                .thenReturn(Optional.of(userToRegister));

        when(environment.getRequiredProperty("registration.activation.time"))
                .thenReturn("120000");

        assertThat(userService.register(createUserDto))
                .isInstanceOf(GetUserDto.class)
                .isEqualTo(userToRegister.toGetUserDto());
    }
}

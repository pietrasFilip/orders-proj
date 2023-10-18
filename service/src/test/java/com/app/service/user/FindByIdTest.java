package com.app.service.user;

import com.app.persistence.data.reader.loader.repository.db.UserRepository;
import com.app.persistence.model.dto.user.GetUserDto;
import com.app.persistence.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindByIdTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    @TestFactory
    @DisplayName("When there is user with such id")
    Stream<DynamicTest> test1() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(User
                        .builder()
                        .id(1L)
                        .username("user")
                        .email("email")
                        .build()));

        return Stream.of(userService.findById(1L))
                        .map(n -> dynamicTest("Is equal to expected",
                                () -> assertThat(n)
                                        .isEqualTo(new GetUserDto(1L, "user", "email"))));

    }

    @Test
    @DisplayName("When there is no user with such id")
    void test2() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(1L))
                .isInstanceOf(IllegalStateException.class);
    }
}

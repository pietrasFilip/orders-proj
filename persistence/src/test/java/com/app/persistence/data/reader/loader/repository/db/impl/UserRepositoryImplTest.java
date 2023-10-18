package com.app.persistence.data.reader.loader.repository.db.impl;

import com.app.persistence.model.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {
    @Mock
    UserRepositoryImpl userRepository;

    @Test
    @DisplayName("When there is user with such email in database")
    void test1() {
        when(userRepository.findByEmail(anyString()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .email("test@gmail.com")
                        .build()));

        assertThat(userRepository.findByEmail(anyString()))
                .isInstanceOf(Optional.class)
                .hasValueSatisfying(user -> assertThat(user)
                        .isInstanceOf(User.class));
    }

    @Test
    @DisplayName("When there is no user with such email in database")
    void test2() {
        when(userRepository.findByEmail(anyString()))
                .thenAnswer(invocationOnMock -> Optional.empty());

        assertThat(userRepository.findByEmail(anyString()))
                .isEmpty();
    }

    @Test
    @DisplayName("When there is user with such username in database")
    void test3() {
        when(userRepository.findByUsername(anyString()))
                .thenAnswer(invocationOnMock -> Optional.of(User.builder()
                        .username("testUser")
                        .build()));

        assertThat(userRepository.findByUsername(anyString()))
                .isInstanceOf(Optional.class)
                .hasValueSatisfying(user -> assertThat(user)
                        .isInstanceOf(User.class));
    }

    @Test
    @DisplayName("When there is no user with such username in database")
    void test4() {
        when(userRepository.findByUsername(anyString()))
                .thenAnswer(invocationOnMock -> Optional.empty());

        assertThat(userRepository.findByUsername(anyString()))
                .isEmpty();
    }
}

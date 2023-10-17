package com.app.service.user;

import com.app.persistence.data.reader.loader.repository.db.UserRepository;
import com.app.persistence.model.dto.token.AuthenticationDto;
import com.app.persistence.model.dto.user.CreateUserDto;
import com.app.persistence.model.dto.user.GetUserDto;
import com.app.persistence.model.dto.validator.AuthenticationDtoValidator;
import com.app.persistence.model.dto.validator.CreateUserDtoValidator;
import com.app.persistence.model.user.User;
import com.app.service.email.EmailService;
import com.app.service.user.exception.UserServiceException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationDtoValidator authenticationDtoValidator;
    private final CreateUserDtoValidator createUserDtoValidator;
    private final Environment environment;
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public GetUserDto register(CreateUserDto createUserDto) {
        createUserDtoValidator.validate(createUserDto);
        var username = createUserDto.username();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserServiceException("Username already exists");
        }

        var email = createUserDto.email();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserServiceException("Email already exists");
        }

        var password = createUserDto.password();
        var userToRegister = createUserDto
                .toUser()
                .withPassword(passwordEncoder.encode(password));

        var insertedUser = userRepository
                .save(userToRegister)
                .map(User::toGetUserDto)
                .orElseThrow(IllegalStateException::new);

        var timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() +
                Long.parseLong(environment.getRequiredProperty("registration.activation.time"));
        emailService.send(
                insertedUser.email(),
                environment.getRequiredProperty("registration.email.subject"),
                environment.getRequiredProperty("registration.email.content") +
                        "http://localhost:8080/users/activate?id=%s&timestamp=%s"
                                .formatted(insertedUser.id(), timestamp)
        );
        return insertedUser;
    }

    @Override
    public GetUserDto activate(Long userId, Long expirationTime) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is null");
        }

        if (expirationTime == null) {
            throw new IllegalArgumentException("Expiration time is null");
        }

        logger.debug(expirationTime);
        logger.debug(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
        if (expirationTime < LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()) {
            throw new IllegalArgumentException("Registration token has been expired");
        }

        return userRepository
                .findById(userId)
                .flatMap(user -> userRepository.update(userId, user.withActiveState(1)))
                .orElseThrow()
                .toGetUserDto();
    }

    @Override
    public GetUserDto findById(Long id) {
        return userRepository
                .findById(id)
                .map(User::toGetUserDto)
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public GetUserDto findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(User::toGetUserDto)
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public GetUserDto findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .map(User::toGetUserDto)
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public Long isUserCorrect(AuthenticationDto authenticationDto) {
        authenticationDtoValidator.validate(authenticationDto);

        var exceptionContent = "Wrong username or password";

        var user = userRepository
                .findByUsername(authenticationDto.username()).
                orElseThrow(() -> {
                    logger.error("User with this username not found");
                    return new UserServiceException(exceptionContent);
                });

        if (user.getIsActive() == 0) {
            logger.error("Account is not activated");
            throw new UserServiceException(exceptionContent);
        }

        if (!passwordEncoder.matches(authenticationDto.password(), user.getPassword())) {
            logger.error("Wrong password");
            throw new UserServiceException("Wrong username or password");
        }

        return user.getId();
    }
}

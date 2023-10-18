package com.app.web.routing;

import com.app.persistence.model.dto.token.AuthenticationDto;
import com.app.persistence.model.dto.token.RefreshTokenDto;
import com.app.service.token.TokensService;
import com.app.service.user.UserService;
import com.app.web.dto.ResponseDto;
import com.app.web.routing.exception.SecurityRoutingException;
import com.app.web.routing.uri.SecurityUri;
import com.app.web.transformer.JsonTransformer;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.app.web.routing.uri.SecurityUri.*;
import static spark.Spark.*;

@Component
public class SecurityRouting {
    private final TokensService tokensService;
    private final JsonTransformer jsonTransformer;
    private final UserService userService;
    private final Gson gson;
    private final Environment environment;
    private final Map<SecurityUri, List<String>> uris;
    private static final Logger logger = LogManager.getRootLogger();

    public SecurityRouting(TokensService tokensService, JsonTransformer jsonTransformer, UserService userService,
                           Gson gson, Environment environment) {
        this.tokensService = tokensService;
        this.jsonTransformer = jsonTransformer;
        this.userService = userService;
        this.gson = gson;
        this.environment = environment;
        this.uris = initUris();
    }

    private Map<SecurityUri, List<String>> initUris() {
        return Map.of(
                ADMIN, Arrays.stream(environment.getRequiredProperty("uri.ADMIN")
                        .split(",")).toList(),
                IS_AUTH, Arrays.stream(environment.getRequiredProperty("uri.IS_AUTH")
                        .split(",")).toList(),
                PERMITTED_ALL, Arrays.stream(environment.getRequiredProperty("uri.PERMITTED_ALL")
                        .split(",")).toList()
        );
    }

    private boolean containsUri(SecurityUri key, String uri) {
        return uris.get(key).stream().anyMatch(uri::startsWith);
    }

    private String prepareToken(String header) {
        if (header == null) {
            logger.error("Header is null");
            throw new SecurityRoutingException("Header is null");
        }

        final var TOKEN_PREFIX = "Bearer ";

        if (!header.startsWith(TOKEN_PREFIX)) {
            logger.error("Header does not start with {}", TOKEN_PREFIX);
            throw new SecurityRoutingException("Header is not correct");
        }

        return header.replace(TOKEN_PREFIX, "");
    }

    public void routes() {
        before((request, response) -> {
            var uriFromRequest = request.uri();

            if (!containsUri(PERMITTED_ALL, uriFromRequest)) {
                var authorizationHeader = request.headers("Authorization");
                var authorizedUser = tokensService.parseTokens(prepareToken(authorizationHeader));

                if (
                        (containsUri(IS_AUTH, uriFromRequest) && !authorizedUser.isAuth()) ||
                                (containsUri(ADMIN, uriFromRequest) && !authorizedUser.isAdmin())
                ) {
                    logger.error("Not authorized user.");
                    throw new SecurityRoutingException("Access Denied!");
                }
            }

        });

        after((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET");
            response.header("Access-Control-Allow-Methods", "POST");
        });

        path("/auth", () -> {
            // http://localhost:8080/auth/login
            post(
                    "/login",
                    (request, response) -> {
                        var authenticationDto = gson.fromJson(
                                request.body(),
                                AuthenticationDto.class);

                        var userId = userService.isUserCorrect(authenticationDto);

                        var tokens = tokensService.generateTokens(userId);
                        response.header("Content-Type", "application/json;charset=utf-8");
                        response.cookie("access", tokens.access(), 3600000, false, true);
                        response.cookie("refresh", tokens.refresh(), 3600000, false, true);
                        return new ResponseDto<>(tokens);
                    },
                    jsonTransformer
            );
            // http://localhost:8080/auth/refresh
            post(
                    "/refresh",
                    (request, response) -> {
                        var refreshTokenDto = gson.fromJson(
                                request.body(),
                                RefreshTokenDto.class);
                        response.header("Content-Type", "application/json;charset=utf-8");
                        return new ResponseDto<>(tokensService.refreshTokens(refreshTokenDto));
                    },
                    jsonTransformer
            );
        });
    }
}

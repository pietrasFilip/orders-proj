package com.app.web.routing;

import com.app.persistence.model.dto.user.CreateUserDto;
import com.app.service.user.UserService;
import com.app.web.dto.ResponseDto;
import com.app.web.transformer.JsonTransformer;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class UserRouting {
    private final UserService userService;
    private final JsonTransformer jsonTransformer;
    private final Gson gson;

    public void routes() {
        path("/users", () -> {
            // http://localhost:8080/users/id/id
            get(
                    "/id/:id",
                    (request, response) -> {
                        var id = Long.parseLong(request.params("id"));
                        response.header("Content-Type", "application/json;charset=utf-8");
                        return new ResponseDto<>(userService.findById(id));
                    },
                    jsonTransformer
            );

            // http://localhost:8080/users/username/username
            get(
                    "/username/:username",
                    (request, response) -> {
                        var username = request.params("username");
                        response.header("Content-Type", "application/json;charset=utf-8");
                        return new ResponseDto<>(userService.findByUsername(username));
                    },
                    jsonTransformer
            );

            // http://localhost:8080/users/email/email
            get(
                    "/email/:email",
                    (request, response) -> {
                        var email = request.params("email");
                        response.header("Content-Type", "application/json;charset=utf-8");
                        return new ResponseDto<>(userService.findByEmail(email));
                    },
                    jsonTransformer
            );

            // http://localhost:8080/users/activate
            get(
                    "/activate",
                    (request, response) -> {
                        var id = Long.parseLong(request.queryParams("id"));
                        var timestamp = Long.parseLong(request.queryParams("timestamp"));
                        response.header("Content-Type", "application/json;charset=utf-8");
                        return new ResponseDto<>(userService.activate(id, timestamp));
                    },
                    jsonTransformer
            );

            // http://localhost:8080/users
            post(
                    "",
                    (request, response) -> {
                        var createUserDto = gson.fromJson(
                                request.body(),
                                CreateUserDto.class);
                        response.header("Content-Type", "application/json;charset=utf-8");
                        response.status(201);
                        return new ResponseDto<>(userService.register(createUserDto));
                    },
                    jsonTransformer
            );
        });
    }
}

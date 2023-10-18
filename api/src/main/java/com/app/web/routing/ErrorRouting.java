package com.app.web.routing;

import com.app.web.dto.ResponseDto;
import com.app.web.transformer.JsonTransformer;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class ErrorRouting {
    private final JsonTransformer jsonTransformer;
    private final Gson gson;
    public void routes() {
        exception(RuntimeException.class, (ex, request, response) -> {
            var exMsg = ex.getMessage();
            response.redirect("/error/" + exMsg, 301);
        });

        path("/error", () -> {
            get(
                    "/:message",
                    (request, response) -> {
                        response.header("Content-Type", "application/json;charset=utf-8");
                        response.status(500);
                        var message = request.params("message");
                        return new ResponseDto<>(message);
                    },
                    jsonTransformer
            );
        });

        internalServerError((request, response) -> {
            response.header("Content-Type", "application/json;charset=utf-8");
            return gson.toJson(new ResponseDto<>("Internal Server Error"));
        });

        notFound((request, response) -> {
            response.header("Content-Type", "application/json;charset=utf-8");
            return gson.toJson(new ResponseDto<>("Resource not found"));
        });
    }
}

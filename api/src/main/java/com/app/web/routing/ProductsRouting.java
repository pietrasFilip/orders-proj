package com.app.web.routing;

import com.app.persistence.data.reader.loader.repository.ProductRepository;
import com.app.web.dto.ResponseDto;
import com.app.web.transformer.JsonTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static spark.Spark.get;
import static spark.Spark.path;

@Component
@RequiredArgsConstructor
public class ProductsRouting {
    private final ProductRepository productRepository;
    private final JsonTransformer jsonTransformer;

    public void routes() {
        // http://localhost:8080/all
        path("/all", () -> {

            // http://localhost:8080/all/products
            path("/products", () -> {

                // http://localhost:8080/all/products/find
                get(
                        "/find",
                        ((request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(productRepository
                                    .findAll()
                                    .stream()
                                    .toList());
                        }),
                        jsonTransformer
                );
            });
        });
    }
}

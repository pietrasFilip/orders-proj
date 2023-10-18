package com.app.web.routing;

import com.app.persistence.model.dto.order.CreateOrderDto;
import com.app.persistence.model.order.Order;
import com.app.service.orders.OrdersService;
import com.app.service.orders.provider.OrdersProvider;
import com.app.web.dto.ResponseDto;
import com.app.web.transformer.JsonTransformer;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static spark.Spark.*;

@Component
@RequiredArgsConstructor
public class OrdersRouting {
    private final OrdersService ordersService;
    private final JsonTransformer jsonTransformer;
    private final OrdersProvider ordersProvider;
    private final Gson gson;
    private final Environment environment;

    public void routes() {
        // http://localhost:8080/is_auth
        path("/is_auth",() -> {

            // http://localhost:8080/is_auth/orders
            path("/orders", () -> {

                // http://localhost:8080/is_auth/orders/average_price/timeFrom/timeTo
                get(
                        "/average_price/:timeFrom/:timeTo",
                        ((request, response) -> {
                            var timeFrom = ZonedDateTime.parse(request.params("timeFrom"));
                            var timeTo = ZonedDateTime.parse(request.params("timeTo"));
                            response.header("Content-Type", "application/json;charset=utf-8");

                            return new ResponseDto<>(ordersService.getAverageOrdersPrice(timeFrom, timeTo));
                        }),
                        jsonTransformer
                );

                // http://localhost:8080/is_auth/orders/max
                get(
                        "/max",
                        ((request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(ordersService.getMaxPriceProductFromCategory());
                        }),
                        jsonTransformer
                );

                // http://localhost:8080/is_auth/orders/purchased_by/email
                get(
                        "/purchased_by/:email",
                        ((request, response) -> {
                            var email = request.params("email");
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(ordersService.getProductsPurchasedByCustomer(email));
                        }),
                        jsonTransformer
                );

                // http://localhost:8080/is_auth/orders/quantity
                path("/quantity", () -> {

                    // http://localhost:8080/is_auth/orders/quantity/date_of/isMin
                    get(
                            "/date_of/:isMin",
                            ((request, response) -> {
                                var minMax = Boolean.getBoolean(request.params("isMin"));
                                response.header("Content-Type", "application/json;charset=utf-8");
                                return new ResponseDto<>(ordersService
                                        .getDateOfMinAndMaxNumberOfOrders(minMax)
                                        .stream()
                                        .map(LocalDate::toString)
                                        .toList()
                                );
                            }),
                            jsonTransformer
                    );
                });

                // http://localhost:8080/is_auth/orders/customer
                path("/customer", () -> {

                    // http://localhost:8080/is_auth/orders/customer/most_expensive
                    get(
                            "/most_expensive",
                            ((request, response) -> {
                                response.header("Content-Type", "application/json;charset=utf-8");
                                return new ResponseDto<>(ordersService.getCustomerWithMostExpensiveOrders());
                            }),
                            jsonTransformer
                    );

                    // http://localhost:8080/is_auth/orders/customer/quantity
                    get(
                            "/:quantity",
                            ((request, response) -> {
                                var quantity = Integer.parseInt(request.params("quantity"));
                                response.header("Content-Type", "application/json;charset=utf-8");
                                return new ResponseDto<>(ordersService
                                        .getCustomersThatOrderedExactlyOrMoreThan(quantity));
                            }),
                            jsonTransformer
                    );
                });

                // http://localhost:8080/is_auth/orders/price/discount
                get(
                        "/price/discount",
                        ((request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            var resultToConvert = ordersService.getOrdersPriceAfterDiscount();

                            return new ResponseDto<>(resultToConvert
                                    .entrySet()
                                    .stream()
                                    .collect(toMap(
                                            order -> order.getKey().toOrderData(),
                                            Map.Entry::getValue
                                    )));
                        }),
                        jsonTransformer
                );

                // http://localhost:8080/is_auth/orders/pdf_list/email
                get(
                        "/pdf_list/:email",
                        ((request, response) -> {
                            var userEmail = request.params("email");
                            var notificationEmail = environment.getRequiredProperty("email.pdf.notification.email.address");
                            var pdfName = environment.getRequiredProperty("email.pdf.attachment.name");
                            var subject = environment.getRequiredProperty("email.pdf.notification.subject");

                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(ordersService.sendPdfEmailWithProductsPurchasedByCustomer(
                                    userEmail, notificationEmail, pdfName, subject, ""));
                        }),
                        jsonTransformer
                );

            });
        });

        // http://localhost:8080/admin
        path("/admin", () -> {

            // http://localhost:8080/admin/orders
            path("/orders", () -> {

                // http://localhost:8080/admin/orders/categories/most_bought
                get(
                        "/categories/most_bought",
                        ((request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(ordersService.getMostBoughtCategory());
                        }),
                        jsonTransformer
                );

                // http://localhost:8080/admin/orders/months/products
                get(
                        "/months/products",
                        ((request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(ordersService.getMonthsWithNumberOfBoughtProductsSortedDescending());
                        }),
                        jsonTransformer
                );

                // http://localhost:8080/admin/orders/categories/most_popular
                get(
                        "/categories/most_popular",
                        ((request, response) -> {
                            response.header("Content-Type", "application/json;charset=utf-8");
                            return new ResponseDto<>(ordersService.getMonthsWithMostPopularCategory());
                        }),
                        jsonTransformer
                );
            });
        });

        // http://localhost:8080/all
        path("/all", () -> {

            // http://localhost:8080/all/orders
            get(
                    "/orders",
                    ((request, response) -> {
                        response.header("Content-Type", "application/json;charset=utf-8");
                        return new ResponseDto<>(ordersProvider.provideOrders()
                                .stream()
                                .map(Order::toOrderData)
                                .toList()
                        );
                    }),
                    jsonTransformer
            );

            post(
                    "",
                    (request, response) -> {
                        var createOrderDto = gson.fromJson(
                                request.body(),
                                CreateOrderDto.class);
                        response.header("Content-Type", "application/json;charset=utf-8");
                        response.status(201);
                        return new ResponseDto<>(ordersService.addOrder(createOrderDto));
                    },
                    jsonTransformer
            );
        });
    }
}

package com.app.service;

import com.app.persistence.model.customer.Customer;
import com.app.persistence.model.order.Order;
import com.app.persistence.model.order.product.Product;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.app.persistence.model.order.product.product_category.Category.*;

public interface Orders {
    Order ORDER_A_1 = Order.of(1L, Customer.of(1L, "A", "A", 28, "a@gmail.com"),
            Product.of(1L, "ZZ", BigDecimal.valueOf(20), A), 5, ZonedDateTime.of(
                    2023, 2, 20, 7, 31, 15, 9000, ZoneId.systemDefault()));

    Order ORDER_B = Order.of(2L, Customer.of(2L, "B", "B", 18, "b@gmail.com"),
            Product.of(2L, "XX", BigDecimal.valueOf(100), B), 1, ZonedDateTime.of(
                    2023, 2, 22, 12, 0, 15, 9000, ZoneId.systemDefault()));

    Order ORDER_A_2 = Order.of(3L, Customer.of(1L, "A", "A", 28, "a@gmail.com"),
            Product.of(3L, "WW", BigDecimal.valueOf(10), A), 3, ZonedDateTime.of(
                    2023, 2, 22, 21, 53, 15, 9000, ZoneId.systemDefault()));

    Order ORDER_C = Order.of(4L, Customer.of(3L, "C", "C", 40, "c@gmail.com"),
            Product.of(4L, "ZX", BigDecimal.valueOf(10), A), 10, ZonedDateTime.of(
                    2023, 2, 20, 4, 31, 15, 9000, ZoneId.systemDefault()));

    Order ORDER_D = Order.of(5L, Customer.of(4L, "D", "D", 28, "d@gmail.com"),
            Product.of(5L, "YY", BigDecimal.valueOf(5), C), 8, ZonedDateTime.of(
                    2023, 2, 22, 14, 12, 15, 9000, ZoneId.systemDefault()));
}
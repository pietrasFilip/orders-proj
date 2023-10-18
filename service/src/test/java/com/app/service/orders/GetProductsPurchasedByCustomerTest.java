package com.app.service.orders;

import com.app.persistence.model.order.product.Product;
import com.app.service.config.AppTestConfig;
import com.app.service.orders.exception.OrdersServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Stream;

import static com.app.persistence.model.order.product.product_category.Category.A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestConfig.class)
@TestPropertySource("classpath:app-test.properties")
class GetProductsPurchasedByCustomerTest {
    @Autowired
    private OrdersService ordersService;

    @Test
    @DisplayName("When there is no customer with given mail")
    void test1() {
        assertThatThrownBy(() -> ordersService.getProductsPurchasedByCustomer("abcd@gmail.com"))
                .isInstanceOf(OrdersServiceException.class)
                .hasMessage("No orders found");
    }

    @TestFactory
    @DisplayName("When customer purchased products")
    Stream<DynamicTest> test2() {
        var expected = Map.of(
                Product.of(1L, "ZZ", BigDecimal.valueOf(20), A), 5,
                Product.of(3L, "WW", BigDecimal.valueOf(10), A), 3
        );

        return Stream.of(ordersService.getProductsPurchasedByCustomer("a@gmail.com"))
                .map(n -> dynamicTest("Is equal to expected",
                        () -> assertThat(n).isEqualTo(expected)));
    }
}

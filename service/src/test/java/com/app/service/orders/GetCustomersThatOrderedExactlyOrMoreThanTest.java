package com.app.service.orders;

import com.app.persistence.model.customer.Customer;
import com.app.service.config.AppTestConfig;
import com.app.service.orders.exception.OrdersServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestConfig.class)
@TestPropertySource("classpath:app-test.properties")
class GetCustomersThatOrderedExactlyOrMoreThanTest {
    @Autowired
    private OrdersService ordersService;

    static Stream<Arguments> argSource() {
        return Stream.of(
                Arguments.of(-1), Arguments.of(-5), Arguments.of(-200), Arguments.of(-10)
                );
    }

    @ParameterizedTest
    @MethodSource("argSource")
    @DisplayName("When number of ordered items is negative")
    void test1(int quantity) {
        assertThatThrownBy(() -> ordersService.getCustomersThatOrderedExactlyOrMoreThan(quantity))
                .isInstanceOf(OrdersServiceException.class)
                .hasMessage("Product quantity is negative number");
    }

    @TestFactory
    @DisplayName("When there is no customer that meets requirements")
    Stream<DynamicTest> test2() {
        var quantity = 12;
        var expected = Collections.<Customer>emptySet();

        assertThat(ordersService.getCustomersThatOrderedExactlyOrMoreThan(quantity))
                .isEqualTo(expected);
        return Stream.of(ordersService.getCustomersThatOrderedExactlyOrMoreThan(quantity))
                .map(n -> dynamicTest("Returns empty set", () -> assertThat(n).isEqualTo(expected)));
    }

    @TestFactory
    @DisplayName("When there are wrong customers")
    Stream<DynamicTest> test3() {
        var quantity = 3;
        var expected = Set.of(
                Customer.of(1L, "A", "A", 28, "a@gmail.com"),
                Customer.of(3L, "C", "C", 40, "c@gmail.com"),
                Customer.of(4L, "D", "D", 28, "d@gmail.com")
        );

        assertThat(ordersService.getCustomersThatOrderedExactlyOrMoreThan(quantity))
                .isEqualTo(expected);
        return Stream.of(ordersService.getCustomersThatOrderedExactlyOrMoreThan(quantity))
                .map(n -> dynamicTest("Is equal to expected", () -> assertThat(n).isEqualTo(expected)));
    }
}

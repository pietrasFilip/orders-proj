package com.app.service.orders;

import com.app.service.config.AppTestConfig;
import com.app.service.orders.exception.OrdersServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestConfig.class)
@TestPropertySource("classpath:app-test.properties")
class GetAverageOrdersPriceTest {

    @Autowired
    private OrdersService ordersService;

    @Test
    @DisplayName("When average price of orders is incorrect")
    void test1() {
        var FROM = ZonedDateTime.of(2023, 2, 20, 10, 0, 0, 9000, ZoneId.systemDefault());
        var TO = ZonedDateTime.of(2023, 2, 22, 21, 0, 0, 9000, ZoneId.systemDefault());

        assertThat(ordersService.getAverageOrdersPrice(FROM, TO))
                .isEqualTo(BigDecimal.valueOf(15.556));
    }

    static Stream<Arguments> argSource() {
        return Stream.of(
                Arguments.of(ZonedDateTime.now().plusSeconds(10), ZonedDateTime.now().minusSeconds(15)),
                Arguments.of(ZonedDateTime.now().plusMinutes(2), ZonedDateTime.now()),
                Arguments.of(ZonedDateTime.now().plusDays(10), ZonedDateTime.now().plusDays(9)),
                Arguments.of(ZonedDateTime.now(), ZonedDateTime.now().minusSeconds(1))
        );
    }

    @ParameterizedTest
    @MethodSource("argSource")
    @DisplayName("When time range is not correct")
    void test2(ZonedDateTime dateTimeFrom, ZonedDateTime dateTimeTo) {
        assertThatThrownBy(() -> ordersService.getAverageOrdersPrice(dateTimeFrom, dateTimeTo))
                .isInstanceOf(OrdersServiceException.class)
                .hasMessage("Wrong date range");
    }

    @TestFactory
    @DisplayName("When there is no data between given time range")
    Stream<DynamicTest> test3() {
        var FROM = ZonedDateTime.of(2023, 2, 20, 10, 0, 0, 9000, ZoneId.systemDefault());
        var TO = ZonedDateTime.of(2023, 2, 22, 10, 0, 0, 9000, ZoneId.systemDefault());
        var expected = BigDecimal.valueOf(0);

        return Stream.of(ordersService.getAverageOrdersPrice(FROM, TO))
                .map(n -> dynamicTest("Equals zero", () -> assertThat(n).isEqualTo(expected)));
    }
}

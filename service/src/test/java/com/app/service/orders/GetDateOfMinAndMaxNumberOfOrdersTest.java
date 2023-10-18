package com.app.service.orders;

import com.app.service.config.AppTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestConfig.class)
@TestPropertySource("classpath:app-test.properties")
class GetDateOfMinAndMaxNumberOfOrdersTest {
    @Autowired
    private OrdersService ordersService;

    @Test
    @DisplayName("When is min date")
    void test1() {
        var expected = List.of(LocalDate.of(2023, 2, 20));

        assertThat(ordersService.getDateOfMinAndMaxNumberOfOrders(true))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("When is max date")
    void test2() {
        var expected = List.of(LocalDate.of(2023, 2, 22));

        assertThat(ordersService.getDateOfMinAndMaxNumberOfOrders(false))
                .isEqualTo(expected);
    }
}

package com.app.service.orders;

import com.app.service.config.AppTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static com.app.Orders.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestConfig.class)
@TestPropertySource("classpath:app-test.properties")
class GetOrdersPriceAfterDiscountTest {
    @Autowired
    private OrdersService ordersService;
    @Test
    @DisplayName("When total price is other than expected")
    void test1() {
        var expectedTotalPrice = Map.of(
                ORDER_A_1,BigDecimal.valueOf(98.00).setScale(2, RoundingMode.CEILING),
                ORDER_B,BigDecimal.valueOf(97.00).setScale(2, RoundingMode.CEILING),
                ORDER_A_2,BigDecimal.valueOf(29.40).setScale(2, RoundingMode.CEILING),
                ORDER_C,BigDecimal.valueOf(98.00).setScale(2, RoundingMode.CEILING),
                ORDER_D,BigDecimal.valueOf(39.20).setScale(2, RoundingMode.CEILING)
        );

        assertThat(ordersService.getOrdersPriceAfterDiscount())
                .containsExactlyInAnyOrderEntriesOf(expectedTotalPrice);
    }
}

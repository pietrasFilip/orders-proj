package com.app.service.orders;

import com.app.persistence.model.customer.Customer;
import com.app.service.config.AppTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestConfig.class)
@TestPropertySource("classpath:app-test.properties")
class GetCustomerWithMostExpensiveOrdersTest {
    @Autowired
    private OrdersService ordersService;

    @Test
    @DisplayName("When customer does not have most expensive orders")
    void test1() {
        var expected = List.of(Customer.of(1L, "A", "A", 28, "a@gmail.com"));
        assertThat(ordersService.getCustomerWithMostExpensiveOrders())
                .isEqualTo(expected);
    }
}

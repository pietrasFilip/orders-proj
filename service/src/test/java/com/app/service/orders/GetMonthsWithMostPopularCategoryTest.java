package com.app.service.orders;

import com.app.service.config.AppTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import java.util.Set;

import static com.app.persistence.model.order.product.product_category.Category.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestConfig.class)
@TestPropertySource("classpath:app-test2.properties")
class GetMonthsWithMostPopularCategoryTest {
    @Autowired
    private OrdersService ordersService;

    @Test
    @DisplayName("When there is wrong category")
    void test1() {
        var expected = Map.of(
                "MAY", Set.of(A),
                "FEBRUARY", Set.of(A, B, C)
        );

        assertThat(ordersService.getMonthsWithMostPopularCategory())
                .containsExactlyInAnyOrderEntriesOf(expected);
    }
}

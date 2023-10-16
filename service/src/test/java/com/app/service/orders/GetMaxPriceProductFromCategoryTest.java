package com.app.service.orders;

import com.app.persistence.model.order.product.Product;
import com.app.service.config.AppTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.app.persistence.model.order.product.product_category.Category.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestConfig.class)
@TestPropertySource("classpath:app-test.properties")
class GetMaxPriceProductFromCategoryTest {
    @Autowired
    private OrdersService ordersService;

    @Test
    @DisplayName("When there is no max price product")
    void test1() {
        var expected = Map.of(
                A, List.of(Product.of(1L, "ZZ", BigDecimal.valueOf(20), A)),
                B, List.of(Product.of(2L, "XX", BigDecimal.valueOf(100), B)),
                C, List.of(Product.of(5L, "YY", BigDecimal.valueOf(5), C))
        );

        assertThat(ordersService.getMaxPriceProductFromCategory())
                .containsExactlyInAnyOrderEntriesOf(expected);
    }
}

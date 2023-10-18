package com.app.persistence.data.reader.processor;

import com.app.persistence.config.AppTestConfig;
import com.app.persistence.model.customer.Customer;
import com.app.persistence.model.order.Order;
import com.app.persistence.model.order.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.app.persistence.model.order.product.product_category.Category.A;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestConfig.class)
@TestPropertySource("classpath:processor-test.properties")
class OrderDataProcessorTxtTest {
    @Autowired
    OrderDataProcessor orderDataTxtProcessor;

    @Test
    @DisplayName("When data source is txt")
    void test1() {
        var expected = List.of(
                Order.of(
                        1L,
                        Customer.of(1L, "A", "A", 25, "a@gmail.com"),
                        Product.of(1L, "B", BigDecimal.valueOf(30), A), 2,
                        ZonedDateTime.of(2023, 12, 3, 10, 15,
                                30, 0, ZoneId.systemDefault())),
                Order.of(
                        2L,
                        Customer.of(2L,"B", "B", 35, "b@gmail.com"),
                        Product.of(2L,"C", BigDecimal.valueOf(20), A), 1,
                        ZonedDateTime.of(2023, 12, 3, 10, 15,
                                30, 0, ZoneId.systemDefault()))
        );

        var path = Paths
                .get("src", "test", "data", "test-orders.txt")
                .toFile()
                .getAbsolutePath();

        assertThat(orderDataTxtProcessor.process(path))
                .hasSize(2)
                .isInstanceOf(List.class)
                .isEqualTo(expected);
    }
}

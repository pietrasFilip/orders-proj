package com.app.service.orders;

import com.app.service.config.AppTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppTestConfig.class)
@TestPropertySource("classpath:app-test2.properties")
class GetMonthsWithNumberOfBoughtProductsTest {

    @Autowired
    private OrdersService ordersService;

    @TestFactory
    @DisplayName("When is not sorted descending by number of products")
    Stream<DynamicTest> test1() {
        var expected = new LinkedHashMap<String, Integer>();
        expected.put("MAY", 15);
        expected.put("FEBRUARY", 12);

        return Stream.of(ordersService.getMonthsWithNumberOfBoughtProductsSortedDescending())
                .map(n -> dynamicTest("Is equal to expected",
                        () -> assertThat(n).containsExactlyEntriesOf(expected)));
    }
}

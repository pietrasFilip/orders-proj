package com.app.persistence.data.reader.loader.order.db;

import com.app.persistence.data.reader.model.CustomerData;
import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.data.reader.model.ProductData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderDataDbLoaderImplTest {
    @Mock
    OrderDataDbLoader orderDataDbLoader;

    @TestFactory
    @DisplayName("When there is data to load from db")
    Stream<DynamicNode> test1() {
        when(orderDataDbLoader.load(""))
                .thenAnswer(invocationOnMock -> List.of(
                        OrderData.of(1L,CustomerData.of(1L,"A", "A", 28, "a@gmail.com"),
                                ProductData.of(1L,"ZZ", BigDecimal.valueOf(20), "A"),
                                5, ""),
                        OrderData.of(2L, CustomerData.of(2L,"B", "B", 18, "b@gmail.com"),
                ProductData.of(2L,"XX", BigDecimal.valueOf(100), "B"), 1,
                                "")
                ));

        return Stream.of(orderDataDbLoader.load(""))
                .map(n -> dynamicContainer(
                        "Container" + n, Stream.of(
                                dynamicTest("Is instance of List",
                                        () -> assertThat(n).isInstanceOf(List.class)),
                                dynamicTest("Is instance of OrderData",
                                        () -> n.forEach(o -> assertThat(o).isInstanceOf(OrderData.class))),
                                dynamicTest("Has exactly size of 2",
                                        () -> assertThat(n).hasSize(2))
                        )
                ));
    }
}

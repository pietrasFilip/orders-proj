package com.app.persistence.data.reader.converter;

import com.app.persistence.data.reader.converter.impl.ToOrderConverterImpl;
import com.app.persistence.data.reader.loader.order.txt.OrderDataTxtLoaderImpl;
import com.app.persistence.model.order.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ToOrderConverterImplTest {

    @TestFactory
    @DisplayName("When there is conversion from OrderData to Order")
    Stream<DynamicNode> test1() {
        var path = new File("src/test/data/test-orders.txt").getAbsoluteFile().getPath();
        var txtLoader = new OrderDataTxtLoaderImpl();
        var data = txtLoader.load(path);

        var converter = new ToOrderConverterImpl();
        return Stream.of(converter.convert(data))
                .map(n -> dynamicContainer(
                        "Container" + n, Stream.of(
                                dynamicTest("Is instance of Order",
                                        () -> n.forEach(o -> assertThat(o)
                                                .isInstanceOf(Order.class))),
                                dynamicTest("Has size of 2",
                                        () -> assertThat(n).hasSize(2))
                        )
                ));
    }
}

package com.app.persistence.data.reader.loader.order.json;

import com.app.persistence.data.reader.model.OrderData;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class OrderDataJsonLoaderImplTest {

    @TestFactory
    @DisplayName("When there is data to load from .json file")
    Stream<DynamicNode> test1() {
        var path = new File("src/test/data/test-orders.json").getAbsoluteFile().getPath();
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var jsonLoader = new OrderDataJsonLoaderImpl(gson);
        return Stream.of(jsonLoader.load(path))
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

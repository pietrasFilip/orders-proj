package com.app.persistence.data.writer.impl;

import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.data.writer.OrderDataToJsonConverter;

import java.util.List;

public class OrderDataToJsonConverterImpl extends ToJsonConverterImpl<List<OrderData>> implements OrderDataToJsonConverter {
    public OrderDataToJsonConverterImpl(String jsonFilename) {
        super(jsonFilename);
    }
}

package com.app.persistence.data.reader.converter.impl;

import com.app.persistence.data.reader.converter.ToOrderConverter;
import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.model.order.Order;

import java.util.List;

public class ToOrderConverterImpl implements ToOrderConverter {
    @Override
    public List<Order> convert(List<OrderData> data) {
        return data
                .stream()
                .map(OrderData::toOrder)
                .toList();
    }
}

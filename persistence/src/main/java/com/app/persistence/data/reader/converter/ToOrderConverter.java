package com.app.persistence.data.reader.converter;

import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.model.order.Order;

import java.util.List;

public interface ToOrderConverter extends Converter<List<OrderData>, List<Order>> {
}

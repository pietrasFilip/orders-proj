package com.app.service.orders.provider;

import com.app.persistence.model.order.Order;

import java.util.List;

public interface OrdersProvider {
    List<Order> provideOrders();
}

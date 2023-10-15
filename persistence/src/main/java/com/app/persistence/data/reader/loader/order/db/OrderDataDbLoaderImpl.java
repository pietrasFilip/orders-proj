package com.app.persistence.data.reader.loader.order.db;

import com.app.persistence.data.reader.loader.repository.OrderRepository;
import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.data.reader.model.db.OrderDataDb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class OrderDataDbLoaderImpl implements OrderDataDbLoader {
    private final OrderRepository orderRepository;

    @Override
    public List<OrderData> load(String path) {
        return orderRepository
                .findAllOrders()
                .stream()
                .map(OrderDataDb::toOrderData)
                .toList();
    }
}

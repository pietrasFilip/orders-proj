package com.app.persistence.data.reader.factory;

import com.app.persistence.data.reader.converter.Converter;
import com.app.persistence.data.reader.converter.impl.ToOrderConverterImpl;
import com.app.persistence.data.reader.loader.DataLoader;
import com.app.persistence.data.reader.loader.order.db.OrderDataDbLoaderImpl;
import com.app.persistence.data.reader.loader.repository.OrderRepository;
import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.data.reader.validator.DataValidator;
import com.app.persistence.data.reader.validator.OrderDataValidator;
import com.app.persistence.model.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class FromDbToOrderWithValidator implements DataFactory<List<OrderData>, List<Order>> {
    private final OrderRepository orderRepository;
    private final OrderDataValidator orderDataValidator;
    @Override
    public DataLoader<List<OrderData>> createDataLoader() {
        return new OrderDataDbLoaderImpl(orderRepository);
    }

    @Override
    public DataValidator<List<OrderData>> createValidator() {
        return orderDataValidator;
    }

    @Override
    public Converter<List<OrderData>, List<Order>> createConverter() {
        return new ToOrderConverterImpl();
    }
}

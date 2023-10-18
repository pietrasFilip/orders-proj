package com.app.persistence.data.reader.factory;

import com.app.persistence.data.reader.converter.Converter;
import com.app.persistence.data.reader.converter.impl.ToOrderConverterImpl;
import com.app.persistence.data.reader.loader.DataLoader;
import com.app.persistence.data.reader.loader.order.txt.OrderDataTxtLoaderImpl;
import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.data.reader.validator.DataValidator;
import com.app.persistence.data.reader.validator.OrderDataValidator;
import com.app.persistence.model.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class FromTxtToOrderWithValidator implements DataFactory<List<OrderData>, List<Order>> {
    private final OrderDataValidator orderDataValidator;
    @Override
    public DataLoader<List<OrderData>> createDataLoader() {
        return new OrderDataTxtLoaderImpl();
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

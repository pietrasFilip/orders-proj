package com.app.persistence.data.reader.processor.impl;

import com.app.persistence.data.reader.converter.Converter;
import com.app.persistence.data.reader.factory.FromTxtToOrderWithValidator;
import com.app.persistence.data.reader.loader.DataLoader;
import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.data.reader.processor.OrderDataProcessor;
import com.app.persistence.data.reader.validator.DataValidator;
import com.app.persistence.model.order.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDataProcessorTxtImpl implements OrderDataProcessor {
    private final DataLoader<List<OrderData>> dataLoader;
    private final DataValidator<List<OrderData>> validator;
    private final Converter<List<OrderData>, List<Order>> converter;

    public OrderDataProcessorTxtImpl(FromTxtToOrderWithValidator dataFactory) {
        this.dataLoader = dataFactory.createDataLoader();
        this.validator = dataFactory.createValidator();
        this.converter = dataFactory.createConverter();
    }

    @Override
    public List<Order> process(String path) {
        var loadedData = dataLoader.load(path);
        var validatedData = validator.validate(loadedData);
        return converter.convert(validatedData);
    }
}

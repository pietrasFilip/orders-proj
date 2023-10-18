package com.app.service.orders.provider;

import com.app.persistence.data.reader.processor.OrderDataProcessor;
import com.app.persistence.data.reader.processor.type.ProcessorType;
import com.app.persistence.model.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersProviderImpl implements OrdersProvider{

    @Value("${processor.type}")
    private String processorType;
    @Value("${processor.json.path}")
    private String jsonPath;
    @Value("${processor.txt.path}")
    private String txtPath;

    private final OrderDataProcessor orderDataDbProcessor;

    private final OrderDataProcessor orderDataJsonProcessor;

    private final OrderDataProcessor orderDataTxtProcessor;

    @Override
    public List<Order> provideOrders() {
        return switch (ProcessorType.valueOf(processorType)) {
            case FROM_DB_TO_ORDER_WITH_VALIDATOR -> orderDataDbProcessor.process("");
            case FROM_JSON_TO_ORDER_WITH_VALIDATOR -> orderDataJsonProcessor.process(jsonPath);
            case FROM_TXT_TO_ORDER_WITH_VALIDATOR -> orderDataTxtProcessor.process(txtPath);
        };
    }
}

package com.app.persistence.data.reader.validator.impl;


import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.data.reader.validator.CustomerDataValidator;
import com.app.persistence.data.reader.validator.OrderDataValidator;
import com.app.persistence.data.reader.validator.ProductDataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.app.persistence.data.reader.validator.DataValidator.validateIntLowerThan;

@Component
@RequiredArgsConstructor
public class OrderDataValidatorImpl implements OrderDataValidator {
    private final CustomerDataValidator customerDataValidator;
    private final ProductDataValidator productDataValidator;
    @Value("${validator.min.quantity}")
    private int minQuantity;

    @Override
    public List<OrderData> validate(List<OrderData> data) {
        return data
                .stream()
                .map(this::validateSingleOrder)
                .toList();
    }

    @Override
    public OrderData validateSingleOrder(OrderData orderData) {
        return new OrderData(
                orderData.getId(),
                customerDataValidator.validate(orderData.getCustomer()),
                productDataValidator.validate(orderData.getProduct()),
                validateIntLowerThan(orderData.getQuantity(), minQuantity),
                orderData.getOrderDate()
        );
    }
}

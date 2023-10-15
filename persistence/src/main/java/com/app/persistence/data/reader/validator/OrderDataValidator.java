package com.app.persistence.data.reader.validator;

import com.app.persistence.data.reader.model.OrderData;

import java.util.List;

public interface OrderDataValidator extends DataValidator<List<OrderData>> {
    OrderData validateSingleOrder(OrderData orderData);
}

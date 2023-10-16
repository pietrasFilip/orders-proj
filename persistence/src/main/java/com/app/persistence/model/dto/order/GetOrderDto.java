package com.app.persistence.model.dto.order;

import java.math.BigDecimal;

public record GetOrderDto(
        String customerName,
        String surname,
        String productName,
        BigDecimal price,
        int quantity,
        String orderDate
) {
}

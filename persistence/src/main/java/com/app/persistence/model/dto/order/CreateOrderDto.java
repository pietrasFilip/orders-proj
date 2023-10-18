package com.app.persistence.model.dto.order;

import com.app.persistence.data.reader.model.db.CustomerDataDb;

public record CreateOrderDto(
        String customerName,
        String surname,
        int age,
        String email,
        Long productId,
        int quantity
) {
    public CustomerDataDb toCustomerDataDb() {
        return CustomerDataDb.builder()
                .name(customerName)
                .surname(surname)
                .age(age)
                .email(email)
                .build();
    }
}

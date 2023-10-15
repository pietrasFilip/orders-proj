package com.app.persistence.data.reader.model.db;

import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.model.dto.order.GetOrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderDataDb {
    private Long id;
    private CustomerDataDb customer;
    private ProductDataDb product;
    private int quantity;
    private String orderDate;

    public OrderData toOrderData() {
        return OrderData.of(id, customer.toCustomerData(), product.toProductData(), quantity, orderDate);
    }

    public GetOrderDto toGetOrderDto() {
        return new GetOrderDto(customer.getName(), customer.getSurname(), product.getName(),
                product.getPrice(), quantity, orderDate);
    }
}

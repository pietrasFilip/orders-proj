package com.app.persistence.data.reader.model;

import com.app.persistence.data.reader.validator.OrderDataValidator;
import com.app.persistence.model.customer.Customer;
import com.app.persistence.model.order.Order;
import com.app.persistence.model.order.product.Product;
import com.app.persistence.model.order.product.product_category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderData {
    private Long id;
    private CustomerData customer;
    private ProductData product;
    private int quantity;
    private String orderDate;

    public Order toOrder() {
        return Order.of(
                id,
                Customer.of(customer.getCustomerId(), customer.getName(), customer.getSurname(),
                        customer.getAge(), customer.getEmail()),
                Product.of(product.getProductId(), product.getName(), product.getPrice(),
                        Category.valueOf(product.getCategory())),
                quantity,
                ZonedDateTime.parse(orderDate)
        );
    }

    public static OrderData of(Long id, CustomerData customer, ProductData product, int quantity, String orderDate) {
        return new OrderData(id, customer, product, quantity, orderDate);
    }
    public static OrderData withValidation(Long id, CustomerData customer, ProductData product, int quantity,
                                           String orderDate, OrderDataValidator orderDataValidator) {
        return orderDataValidator.validateSingleOrder(new OrderData(id, customer, product, quantity, orderDate));
    }
}

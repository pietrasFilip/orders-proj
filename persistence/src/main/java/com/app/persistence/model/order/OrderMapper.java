package com.app.persistence.model.order;

import com.app.persistence.model.customer.Customer;
import com.app.persistence.model.order.product.Product;
import com.app.persistence.model.order.product.product_category.Category;

import java.time.LocalDate;
import java.util.function.Function;

import static com.app.persistence.model.order.product.ProductMapper.toCategory;


public interface OrderMapper {
    Function<Order, Product> toProduct = order -> order.product;
    Function<Order, Integer> toQuantity = order -> order.quantity;
    Function<Order, LocalDate> toOrderLocalDate = order -> order.orderDate.toLocalDate();
    Function<Order, Customer> toCustomer = order -> order.customer;
    Function<Order, Category> orderToCategory = order -> toCategory.apply(order.product);
    Function<Order, String> toOrderMonthName = order -> order.orderDate.toLocalDate().getMonth().name();
}

package com.app.persistence.model.order.product;

import com.app.persistence.model.order.product.product_category.Category;

import java.math.BigDecimal;
import java.util.function.Function;

public interface ProductMapper {
    Function<Product, BigDecimal> toPrice = product -> product.price;
    Function<Product, Category> toCategory = product -> product.category;
    Function<Product, String> toProductName = product -> product.name;
}

package com.app.persistence.model.order.product;

import com.app.persistence.data.reader.model.ProductData;
import com.app.persistence.model.order.product.product_category.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class Product {
    Long id;
    String name;
    BigDecimal price;
    Category category;

    // Methods from Object class

    @Override
    public String toString() {
        return "PRODUCT(name=%s, price=%s, category=%s)".formatted(name, price, category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(name, product.name) && Objects.equals(price, product.price) && category == product.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category);
    }

    // Convert methods

    public ProductData toProductData() {
        return ProductData.of(id, this.name, this.price, this.category.toString());
    }

    // Static methods

    public static Product of(Long id, String name, BigDecimal price, Category category) {
        return new Product(id, name, price, category);
    }
}

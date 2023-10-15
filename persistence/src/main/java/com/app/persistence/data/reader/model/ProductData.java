package com.app.persistence.data.reader.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductData {
    private Long productId;
    private String name;
    private BigDecimal price;
    private String category;

    public static ProductData of(Long id, String name, BigDecimal price, String category) {
        return new ProductData(id, name, price, category);
    }
}


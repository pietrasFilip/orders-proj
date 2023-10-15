package com.app.persistence.data.reader.model.db;

import com.app.persistence.data.reader.model.ProductData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDataDb {
    private Long id;
    private String name;
    private BigDecimal price;
    private String category;

    public ProductData toProductData() {
        return ProductData.of(id, name, price, category);
    }
}

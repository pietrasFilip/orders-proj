package com.app.persistence.data.reader.validator.impl;

import com.app.persistence.data.reader.model.ProductData;
import com.app.persistence.data.reader.validator.ProductDataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.app.persistence.data.reader.validator.DataValidator.*;

@Component
@RequiredArgsConstructor
public class ProductDataValidatorImpl implements ProductDataValidator {
    @Value("${validator.regex.name}")
    private String regex;

    @Override
    public ProductData validate(ProductData productData) {
        return ProductData.of(
                productData.getProductId(),
                validateMatchesRegex(regex, productData.getName()),
                validatePositiveDecimal(productData.getPrice()),
                validateNull(productData.getCategory())
        );
    }
}

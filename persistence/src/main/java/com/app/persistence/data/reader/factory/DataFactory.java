package com.app.persistence.data.reader.factory;

import com.app.persistence.data.reader.converter.Converter;
import com.app.persistence.data.reader.loader.DataLoader;
import com.app.persistence.data.reader.validator.DataValidator;
import org.springframework.stereotype.Component;

@Component
public sealed interface DataFactory <T, U> permits FromDbToOrderWithValidator, FromJsonToOrderWithValidator, FromTxtToOrderWithValidator {
    DataLoader<T> createDataLoader();
    DataValidator<T> createValidator();
    Converter<T, U> createConverter();
}

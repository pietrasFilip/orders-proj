package com.app.persistence.data.reader.validator;

import com.app.persistence.data.reader.validator.exception.ValidatorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.app.persistence.data.reader.validator.DataValidator.validateNull;
import static org.assertj.core.api.Assertions.*;

class ValidateNullTest {
    @Test
    @DisplayName("When Category is null")
    void test1() {
        assertThatThrownBy(() -> validateNull(null))
                .isInstanceOf(ValidatorException.class)
                .hasMessage("Is null");
    }
}

package com.app.persistence.model.dto.validator;

import com.app.persistence.model.dto.validator.exception.DtoException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.app.persistence.model.dto.validator.generic.DtoValidator.validateNull;

class DtoValidatorTest {

    @Test
    @DisplayName("When is null")
    void test1() {
        Assertions.assertThatThrownBy(() -> validateNull(null, ""))
                .isInstanceOf(DtoException.class)
                .hasMessage("Is null or empty");
    }

    @Test
    @DisplayName("When is empty")
    void test2() {
        Assertions.assertThatThrownBy(() -> validateNull("", ""))
                .isInstanceOf(DtoException.class)
                .hasMessage("Is null or empty");
    }
}

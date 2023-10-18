package com.app.persistence.data.reader.validator;

import com.app.persistence.data.reader.validator.exception.ValidatorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.app.persistence.data.reader.validator.DataValidator.validatePositiveDecimal;
import static org.assertj.core.api.Assertions.*;

class ValidatePositiveDecimalTest {

    static Stream<Arguments> argSource() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-0.01)),
                Arguments.of(BigDecimal.valueOf(-10.01)),
                Arguments.of(BigDecimal.valueOf(-0.41)),
                Arguments.of(BigDecimal.valueOf(-110.98)),
                Arguments.of(BigDecimal.valueOf(-2000)));
    }
    @ParameterizedTest
    @MethodSource("argSource")
    @DisplayName("When its negative decimal")
    void test1(BigDecimal value) {
        assertThatThrownBy(() -> validatePositiveDecimal(value))
                .isInstanceOf(ValidatorException.class)
                .hasMessage("Value is zero or less");
    }
}

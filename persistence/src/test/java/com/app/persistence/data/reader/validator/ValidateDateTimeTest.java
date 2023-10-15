package com.app.persistence.data.reader.validator;

import com.app.persistence.data.reader.validator.exception.ValidatorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static com.app.persistence.data.reader.validator.DataValidator.validateDateTime;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidateDateTimeTest {

    static Stream<Arguments> argSource() {
        return Stream.of(
                Arguments.of(ZonedDateTime.now().minusHours(2)), Arguments.of(ZonedDateTime.now().minusMinutes(1)),
                Arguments.of(ZonedDateTime.now().minusSeconds(15)), Arguments.of(ZonedDateTime.now().minusDays(1))
                );
    }

    @ParameterizedTest
    @MethodSource("argSource")
    @DisplayName("When given date time is from past")
    void test1(ZonedDateTime dateTime) {
        assertThatThrownBy(() -> validateDateTime(dateTime))
                .isInstanceOf(ValidatorException.class)
                .hasMessage("Wrong order date time");
    }
}

package com.app.persistence.data.reader.validator;

import com.app.persistence.data.reader.validator.exception.ValidatorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface DataValidator<T> {
    Logger logger = LogManager.getRootLogger();
    T validate(T t);

    static String validateMatchesRegex(String regex, String model) {
        if (model == null || model.isEmpty()) {
            logger.error("Model is null or empty");
            throw new ValidatorException("Model is null or empty");
        }

        if (!model.matches(regex)) {
            logger.error("Model does not match regex");
            throw new ValidatorException("Model does not match regex");
        }
        return model;
    }

    static BigDecimal validatePositiveDecimal(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Value is zero or less");
            throw new ValidatorException("Value is zero or less");
        }
        return value;
    }

    static int validateIntLowerThan(int value, int minValue) {
        if (value < minValue) {
            logger.error("Value is lower than min value");
            throw new ValidatorException("Value is lower than min value");
        }
        return value;
    }

    static ZonedDateTime validateDateTime(ZonedDateTime dateTime) {
        if (dateTime.toInstant().plusMillis(1000).isBefore(ZonedDateTime.now().toInstant())) {
            logger.error("Order date is from the past");
            throw new ValidatorException("Wrong order date time");
        }

        return dateTime;
    }

    static <T> T validateNull(T t) {
        if (t == null) {
            logger.error("Given object is null");
            throw new ValidatorException("Is null");
        }
        return t;
    }

}

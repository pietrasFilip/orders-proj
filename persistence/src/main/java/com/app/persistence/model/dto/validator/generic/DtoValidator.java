package com.app.persistence.model.dto.validator.generic;


import com.app.persistence.model.dto.validator.exception.DtoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface DtoValidator<T> {
    Logger logger = LogManager.getRootLogger();

    void validate(T t);

    static <T> T validateNull(T t, String message) {
        if (t == null || t.toString().isEmpty()) {
            logger.error(message);
            throw new DtoException("Is null or empty");
        }
        return t;
    }
}

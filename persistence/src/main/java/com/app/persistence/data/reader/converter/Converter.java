package com.app.persistence.data.reader.converter;

public interface Converter <T, U> {
    U convert(T data);
}

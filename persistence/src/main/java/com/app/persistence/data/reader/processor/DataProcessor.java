package com.app.persistence.data.reader.processor;

public interface DataProcessor <T>{
    T process(String path);
}

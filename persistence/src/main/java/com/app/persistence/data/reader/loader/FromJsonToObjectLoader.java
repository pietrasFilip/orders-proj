package com.app.persistence.data.reader.loader;


import com.app.persistence.data.reader.loader.exception.JsonLoaderException;
import com.google.gson.Gson;

import java.io.FileReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class FromJsonToObjectLoader<T> {
    private final Gson gson;
    private final Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    protected FromJsonToObjectLoader(Gson gson) {
        this.gson = gson;
    }

    public T loadObject(String path) {
        try(var fileReader = new FileReader(path)) {
            return gson.fromJson(fileReader, type);
        } catch (Exception e) {
            throw new JsonLoaderException(e.getMessage());
        }
    }
}

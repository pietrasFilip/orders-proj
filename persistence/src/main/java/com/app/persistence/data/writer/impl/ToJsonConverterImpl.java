package com.app.persistence.data.writer.impl;

import com.app.persistence.data.reader.loader.exception.JsonLoaderException;
import com.app.persistence.data.writer.ToJsonConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;

public class ToJsonConverterImpl<T> implements ToJsonConverter<T> {
    private final String jsonFilename;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ToJsonConverterImpl(String jsonFilename) {
        this.jsonFilename = jsonFilename;
    }

    @Override
    public void to(T t) {
        try(var fileWriter = new FileWriter(jsonFilename)) {
            gson.toJson(t, fileWriter);
        } catch (Exception e) {
            throw new JsonLoaderException(e.getMessage());
        }
    }
}

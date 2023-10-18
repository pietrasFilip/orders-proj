package com.app.persistence.data.reader.loader;

public interface DataLoader<T> {
    T load(String path);
}

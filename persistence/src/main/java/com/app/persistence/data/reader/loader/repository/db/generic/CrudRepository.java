package com.app.persistence.data.reader.loader.repository.db.generic;

import java.util.List;
import java.util.Optional;

public interface CrudRepository <T, ID> {
    Optional<T> save(T t);
    List<T> saveAll(List<T> items);
    Optional<T> update(ID id, T item);
    List<T> updateAll(List<T> items);
    Optional<T> findById(ID id);
    List<T> findLast(int n);
    List<T> findAll();
    Optional<T> deleteById(ID id);
    List<T> deleteAll();
}

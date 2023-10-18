package com.app.persistence.data.reader.loader.repository;

import com.app.persistence.data.reader.loader.repository.db.generic.CrudRepository;
import com.app.persistence.data.reader.model.db.ProductDataDb;

import java.util.Optional;


public interface ProductRepository extends CrudRepository<ProductDataDb, Long> {
    Optional<ProductDataDb> findByName(String name);
}

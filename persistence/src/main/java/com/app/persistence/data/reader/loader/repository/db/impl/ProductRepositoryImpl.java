package com.app.persistence.data.reader.loader.repository.db.impl;

import com.app.persistence.data.reader.loader.repository.ProductRepository;
import com.app.persistence.data.reader.loader.repository.db.generic.AbstractCrudRepository;
import com.app.persistence.data.reader.loader.repository.db.impl.exception.ProductRepositoryException;
import com.app.persistence.data.reader.model.db.ProductDataDb;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductRepositoryImpl extends AbstractCrudRepository<ProductDataDb, Long> implements ProductRepository {
    public ProductRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public Optional<ProductDataDb> findByName(String name) {
        try {
            var sql = "select * from products where name=:name";
            return jdbi.withHandle(handle -> handle
                    .createQuery(sql)
                    .bind("name", name)
                    .mapToBean(ProductDataDb.class)
                    .findFirst()
            );
        } catch (Exception e) {
            throw new ProductRepositoryException(e.getMessage());
        }
    }
}

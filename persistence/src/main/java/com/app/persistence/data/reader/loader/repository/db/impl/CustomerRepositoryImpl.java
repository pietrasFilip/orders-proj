package com.app.persistence.data.reader.loader.repository.db.impl;

import com.app.persistence.data.reader.loader.repository.CustomerRepository;
import com.app.persistence.data.reader.loader.repository.db.generic.AbstractCrudRepository;
import com.app.persistence.data.reader.loader.repository.db.impl.exception.CustomerRepositoryException;
import com.app.persistence.data.reader.model.db.CustomerDataDb;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CustomerRepositoryImpl extends AbstractCrudRepository<CustomerDataDb, Long> implements CustomerRepository {
    public CustomerRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public Optional<CustomerDataDb> findByEmail(String email) {
        try {
            var sql = "select * from customers where email=:email";
            return jdbi.withHandle(handle -> handle
                    .createQuery(sql)
                    .bind("email", email)
                    .mapToBean(CustomerDataDb.class)
                    .findFirst()
            );
        } catch (Exception e) {
            throw new CustomerRepositoryException(e.getMessage());
        }
    }
}

package com.app.persistence.data.reader.loader.repository;

import com.app.persistence.data.reader.loader.repository.db.generic.CrudRepository;
import com.app.persistence.data.reader.model.db.CustomerDataDb;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<CustomerDataDb, Long> {
    Optional<CustomerDataDb> findByEmail(String email);
}

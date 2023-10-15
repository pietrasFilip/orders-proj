package com.app.persistence.data.reader.loader.repository.db;

import com.app.persistence.data.reader.loader.repository.db.generic.CrudRepository;
import com.app.persistence.model.user.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}

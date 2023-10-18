package com.app.persistence.data.reader.loader.repository.db.impl;

import com.app.persistence.data.reader.loader.repository.db.UserRepository;
import com.app.persistence.data.reader.loader.repository.db.generic.AbstractCrudRepository;
import com.app.persistence.data.reader.loader.repository.db.impl.exception.UserRepositoryException;
import com.app.persistence.model.user.User;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends AbstractCrudRepository<User, Long> implements UserRepository {
    protected UserRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            var sql = "select * from users where username=:username";
            return jdbi.withHandle(handle -> handle
                    .createQuery(sql)
                    .bind("username", username)
                    .mapToBean(User.class)
                    .findFirst()
            );
        } catch (Exception e) {
            throw new UserRepositoryException(e.getMessage());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            var sql = "select * from users where email=:email";
            return jdbi.withHandle(handle -> handle
                    .createQuery(sql)
                    .bind("email", email)
                    .mapToBean(User.class)
                    .findFirst()
            );
        } catch (Exception e) {
            throw new UserRepositoryException(e.getMessage());
        }
    }
}

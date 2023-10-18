package com.app.persistence.data.reader.loader.repository;

import com.app.persistence.data.reader.loader.repository.db.generic.CrudRepository;
import com.app.persistence.data.reader.model.db.OrderDataDb;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<OrderDataDb, Long> {
    List<OrderDataDb> findAllOrders();
    Optional<OrderDataDb> saveOrder(OrderDataDb order);
}

package com.app.persistence.data.reader.loader.repository.db.impl;

import com.app.persistence.data.reader.loader.repository.OrderRepository;
import com.app.persistence.data.reader.loader.repository.db.generic.AbstractCrudRepository;
import com.app.persistence.data.reader.loader.repository.db.impl.exception.OrderRepositoryException;
import com.app.persistence.data.reader.model.db.CustomerDataDb;
import com.app.persistence.data.reader.model.db.OrderDataDb;
import com.app.persistence.data.reader.model.db.ProductDataDb;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl extends AbstractCrudRepository<OrderDataDb, Long> implements OrderRepository {

    public OrderRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }
    @Override
    public List<OrderDataDb> findAllOrders() {
        var sql = """
                select orders.id as order_id, c.id as customer_id, c.name as customer_name, c.surname, c.age, c.email,
                p.id as product_id, p.name as product_name, p.price, p.category, orders.quantity, orders.order_date
                from orders
                join customers c on orders.customer_id = c.id
                join products p on p.id = orders.product_id
                """;

        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .map((rs, ctx) -> OrderDataDb
                        .builder()
                        .id(rs.getLong("order_id"))
                        .customer(CustomerDataDb
                                .builder()
                                .id(rs.getLong("customer_id"))
                                .name(rs.getString("customer_name"))
                                .surname(rs.getString("surname"))
                                .age(rs.getInt("age"))
                                .email(rs.getString("email"))
                                .build())
                        .product(ProductDataDb
                                .builder()
                                .id(rs.getLong("product_id"))
                                .name(rs.getString("product_name"))
                                .price(rs.getBigDecimal("price"))
                                .category(rs.getString("category"))
                                .build())
                        .quantity(rs.getInt("quantity"))
                        .orderDate(rs.getString("order_date"))
                        .build())
                .list()
        );
    }

    @Override
    public Optional<OrderDataDb> saveOrder(OrderDataDb order) {
        var sql = """
                  insert into orders (customer_id, product_id, quantity, order_date)
                  values (:customerId, :productId, :quantity, :orderDate)
                  """;
        try {
            jdbi.withHandle(handle -> handle
                    .createUpdate(sql)
                    .bind("customerId", order.getCustomer().getId())
                    .bind("productId", order.getProduct().getId())
                    .bind("quantity", order.getQuantity())
                    .bind("orderDate", order.getOrderDate())
                    .execute()
            );
        } catch (Exception e) {
            throw new OrderRepositoryException(e.getMessage());
        }
        return Optional.of(order);
    }
}

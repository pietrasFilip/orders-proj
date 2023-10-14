package com.app.persistence.model.order;

import com.app.persistence.data.reader.model.OrderData;
import com.app.persistence.model.customer.Customer;
import com.app.persistence.model.order.product.Product;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

import static com.app.persistence.model.order.product.ProductMapper.toPrice;
import static java.math.BigDecimal.valueOf;

@RequiredArgsConstructor
public class Order {
    final Long id;
    final Customer customer;
    final Product product;
    final int quantity;
    final ZonedDateTime orderDate;
    private static final BigDecimal ORDER_DATE_DISCOUNT_PERCENT = BigDecimal.valueOf(0.98);
    private static final BigDecimal AGE_DISCOUNT_PERCENT = BigDecimal.valueOf(0.97);
    private static final int DISCOUNT_AGE = 25;

    // Methods that give information about order

    /**
     *
     * @param email Email address to check.
     * @return True when customer has given mail or false when not.
     */
    public boolean containsEmail(String email) {
        return customer.hasEmail(email);
    }

    /**
     *
     * @param from Starting date.
     * @param to Ending date.
     * @return True when order date is between given dates or false when not.
     */
    public boolean hasDateBetween(ZonedDateTime from, ZonedDateTime to) {
        return orderDate.isAfter(from) && orderDate.isBefore(to);
    }

    /**
     *
     * @return Total price of order.
     */
    public BigDecimal totalPrice() {
        return toPrice.apply(product).multiply(valueOf(quantity));
    }

    /**
     *
     * @return Total price of order with discount.
     */
    public BigDecimal totalPriceWithDiscount() {
        if (customer.hasDiscountAge(DISCOUNT_AGE)) {
            return totalPrice().multiply(AGE_DISCOUNT_PERCENT);
        }
        if (orderDate.isBefore(ZonedDateTime.now().plusDays(2))){
            return totalPrice().multiply(ORDER_DATE_DISCOUNT_PERCENT);
        }
        return totalPrice();
    }

    // Methods from Object class

    @Override
    public String toString() {
        return "ORDER(customer=%s, product=%s, quantity=%s, order date=%s)".formatted(customer, product, quantity, orderDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return quantity == order.quantity && Objects.equals(customer, order.customer) && Objects.equals(product, order.product) && Objects.equals(orderDate, order.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, product, quantity, orderDate);
    }

    // Convert methods

    public Map.Entry<String, Customer> toCustomerMailEntry() {
        return customer.toEntry();
    }

    public Map.Entry<Product, Integer> toNumberOfProductsInOrderEntry() {
        return Map.entry(this.product, this.quantity);
    }

    public OrderData toOrderData() {
        return new OrderData(id, this.customer.toCustomerData(), this.product.toProductData(), this.quantity,
                this.orderDate.toString());
    }

    // Static methods

    public static Order of(Long id, Customer customer, Product product, int quantity, ZonedDateTime orderDate) {
        return new Order(id, customer, product, quantity, orderDate);
    }
}

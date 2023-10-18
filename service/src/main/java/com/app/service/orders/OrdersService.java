package com.app.service.orders;

import com.app.persistence.model.customer.Customer;
import com.app.persistence.model.dto.email.CreateEmailDto;
import com.app.persistence.model.dto.order.CreateOrderDto;
import com.app.persistence.model.dto.order.GetOrderDto;
import com.app.persistence.model.order.Order;
import com.app.persistence.model.order.product.Product;
import com.app.persistence.model.order.product.product_category.Category;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public sealed interface OrdersService permits OrdersServiceImpl {
    BigDecimal getAverageOrdersPrice(ZonedDateTime from, ZonedDateTime to);
    Map<Category, List<Product>> getMaxPriceProductFromCategory();
    Map<Product, Integer> getProductsPurchasedByCustomer(String email);
    CreateEmailDto sendPdfEmailWithProductsPurchasedByCustomer(String email, String notificationEmail, String filename,
                                                               String subject, String emailContent);
    List<LocalDate> getDateOfMinAndMaxNumberOfOrders(boolean min);
    List<Customer> getCustomerWithMostExpensiveOrders();
    Map<Order, BigDecimal> getOrdersPriceAfterDiscount();
    Set<Customer> getCustomersThatOrderedExactlyOrMoreThan(int quantity);
    Set<Category> getMostBoughtCategory();
    Map<String, Integer> getMonthsWithNumberOfBoughtProductsSortedDescending();
    Map<String, Set<Category>> getMonthsWithMostPopularCategory();
    GetOrderDto addOrder(CreateOrderDto createOrderDto);
}
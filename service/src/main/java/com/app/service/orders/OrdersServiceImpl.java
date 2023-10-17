package com.app.service.orders;

import com.app.persistence.data.reader.loader.repository.CustomerRepository;
import com.app.persistence.data.reader.loader.repository.OrderRepository;
import com.app.persistence.data.reader.loader.repository.ProductRepository;
import com.app.persistence.data.reader.model.db.OrderDataDb;
import com.app.persistence.model.customer.Customer;
import com.app.persistence.model.dto.email.CreateEmailDto;
import com.app.persistence.model.dto.order.CreateOrderDto;
import com.app.persistence.model.dto.order.GetOrderDto;
import com.app.persistence.model.order.Order;
import com.app.persistence.model.order.product.Product;
import com.app.persistence.model.order.product.product_category.Category;
import com.app.persistence.model.dto.validator.CreateOrderDtoValidator;
import com.app.service.email.EmailService;
import com.app.service.orders.exception.OrdersServiceException;
import com.app.service.orders.provider.OrdersProvider;
import com.app.service.pdf.PdfService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import static com.app.persistence.data.reader.validator.DataValidator.validateDateTime;
import static com.app.persistence.model.order.OrderMapper.*;
import static com.app.persistence.model.order.product.ProductMapper.*;
import static java.util.stream.Collectors.*;
@Service
public final class OrdersServiceImpl implements OrdersService{
    final ArrayList<Order> orders;
    private final EmailService emailService;
    private final PdfService pdfService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CreateOrderDtoValidator createOrderDtoValidator;
    private static final Logger logger = LogManager.getRootLogger();

    public OrdersServiceImpl(OrdersProvider ordersProvider, EmailService emailService, PdfService pdfService,
                             OrderRepository orderRepository, CustomerRepository customerRepository,
                             ProductRepository productRepository, CreateOrderDtoValidator createOrderDtoValidator) {
        this.orders = new ArrayList<>(ordersProvider.provideOrders());
        this.emailService = emailService;
        this.pdfService = pdfService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.createOrderDtoValidator = createOrderDtoValidator;
    }

    /**
     *
     * @param from Start of time interval.
     * @param to   End of time interval.
     * @return Average price of all orders from list matching given time interval.
     */
    @Override
    public BigDecimal getAverageOrdersPrice(ZonedDateTime from, ZonedDateTime to) {
        if (from.isAfter(to)) {
            logger.error("Wrong date range given");
            throw new OrdersServiceException("Wrong date range");
        }

        if (numberOfProductsInOrdersInGivenTime(from, to) == 0) {
            return BigDecimal.valueOf(0);
        }

        return orders
                .stream()
                .filter(order -> order.hasDateBetween(from, to))
                .map(Order::totalPrice)
                .reduce(BigDecimal::add)
                .orElseThrow()
                .divide(BigDecimal.valueOf(numberOfProductsInOrdersInGivenTime(from, to)), 3,
                        RoundingMode.HALF_UP);
    }

    private int numberOfProductsInOrdersInGivenTime(ZonedDateTime from, ZonedDateTime to) {
        return orders
                .stream()
                .filter(order -> order.hasDateBetween(from, to))
                .map(toQuantity)
                .reduce(Integer::sum)
                .orElse(0);
    }

    /**
     *
     * @return Map of most expensive products from category.
     */
    @Override
    public Map<Category, List<Product>> getMaxPriceProductFromCategory() {
        return orders
                .stream()
                .map(toProduct)
                .toList()
                .stream()
                .collect(groupingBy(toCategory,
                        collectingAndThen(
                                groupingBy(toPrice),
                                groupedByPrice -> groupedByPrice
                                        .entrySet()
                                        .stream()
                                        .max(Map.Entry.comparingByKey())
                                        .map(Map.Entry::getValue)
                                        .orElseThrow()
                        )));

    }

    /**
     *
     * @param email Email used to identify a customer.
     * @return All products purchased by chosen customer.
     */
    @Override
    public Map<Product, Integer> getProductsPurchasedByCustomer(String email) {
        var customer = customersWithMails().get(email);

        if (customer == null) {
            logger.error("No order associated with this email");
            throw new OrdersServiceException("No orders found");
        }

        return orders
                .stream()
                .filter(order -> order.containsEmail(email))
                .map(Order::toNumberOfProductsInOrderEntry)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        Integer::sum));
    }

    private Map<String, Customer> customersWithMails() {
        return orders
                .stream()
                .map(Order::toCustomerMailEntry)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (m1, m2) -> m1));
    }

    /**
     *
     * @param email             Used for searching all products purchased by customer and for sending mail.
     * @param notificationEmail Email address where notification about sent mail is sent.
     * @param filename          Name of sending file (in this case .pdf file).
     * @param subject           Email subject.
     * @param emailContent      Content of email message.
     */
    @Override
    public CreateEmailDto sendPdfEmailWithProductsPurchasedByCustomer(String email, String notificationEmail, String filename,
                                                                      String subject, String emailContent) {
        var content = new ArrayList<String>();
        getProductsPurchasedByCustomer(email)
                .forEach((key1, value) -> content.add(
                        ("PRODUCT %s, QUANTITY %s").formatted(toProductName.apply(key1), value)));
        emailService.sendWithAttachmentAndNotification(email, notificationEmail, subject,
                emailContent, filename,pdfService.generatePDF(content, filename));
        return new  CreateEmailDto("Mail sent");
    }

    /**
     *
     * @param min Boolean value. When true - get the oldest order date. When false - get the latest order date.
     * @return  The oldest or the latest order date.
     */
    @Override
    public List<LocalDate> getDateOfMinAndMaxNumberOfOrders(boolean min) {
        return getMinMaxOrdersDate(min);
    }

    private List<LocalDate> getMinMaxOrdersDate(boolean min) {
        var ordersDateStream = getNumberOfOrdersInDate()
                .entrySet()
                .stream();
        return min ?
                ordersDateStream.
                        min(Map.Entry.comparingByKey()).
                        map(Map.Entry::getValue).orElseThrow() :
                ordersDateStream.
                        max(Map.Entry.comparingByKey()).
                        map(Map.Entry::getValue).orElseThrow();
    }

    /**
     *
     * @return  Map with number of orders made each day.
     */
    private Map<Integer, List<LocalDate>> getNumberOfOrdersInDate() {
        return orders
                .stream()
                .collect(groupingBy(toOrderLocalDate))
                .entrySet()
                .stream()
                .collect(toMap(key -> key.getValue().size(),
                        val -> new ArrayList<>(List.of(val.getKey())),(v1, v2) -> {
                            v1.addAll(v2);
                            return v1;
                        }));
    }

    /**
     *
     * @return  List of customers who spent the most money on orders.
     */
    @Override
    public List<Customer> getCustomerWithMostExpensiveOrders() {
        return customersWithOrdersTotalPrice()
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElseThrow();
    }

    /**
     *
     * @return  Map with orders total price and customers who spent specified amount of money.
     */
    private Map<BigDecimal, List<Customer>> customersWithOrdersTotalPrice() {
        return orders
                .stream()
                .collect(groupingBy(toCustomer))
                .entrySet()
                .stream()
                .collect(toMap(
                        key -> key.getValue()
                                .stream()
                                .map(Order::totalPrice)
                                .reduce(BigDecimal::add)
                                .orElseThrow(),
                        value -> new ArrayList<>(List.of(value.getKey())),
                        (v1, v2) -> {
                            v1.addAll(v2);
                            return v1;
                        }
                ));
    }

    /**
     *
     * @return Map of orders and their price after discount
     */
    @Override
    public Map<Order, BigDecimal> getOrdersPriceAfterDiscount() {
        return orders
                .stream()
                .collect(toMap(
                        order -> order,
                        Order::totalPriceWithDiscount,
                        BigDecimal::add
                ));
    }

    /**
     *
     * @param quantity  Number of ordered products.
     * @return  Set of customers who made an order with exactly or more than specified quantity.
     */
    @Override
    public Set<Customer> getCustomersThatOrderedExactlyOrMoreThan(int quantity) {

        if (quantity < 0) {
            throw new OrdersServiceException("Product quantity is negative number");
        }

        return orders
                .stream()
                .filter(order -> toQuantity.apply(order) >= quantity)
                .map(toCustomer)
                .collect(toSet());
    }

    /**
     *
     * @return  Set of categories from which products were old the most.
     */
    @Override
    public Set<Category> getMostBoughtCategory() {
        return getNumberOfSalesInCategory()
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .orElseThrow();
    }

    private Map<Integer, Set<Category>> getNumberOfSalesInCategory() {
        return orders
                .stream()
                .collect(groupingBy(orderToCategory))
                .entrySet()
                .stream()
                .collect(toMap(key -> key.getValue().size(),
                        val -> new HashSet<>(Set.of(val.getKey())),(v1, v2) -> {
                    v1.addAll(v2);
                    return v1;
                        }));
    }

    /**
     *
     * @return  Map of number of sold products each month.
     */
    @Override
    public Map<String, Integer> getMonthsWithNumberOfBoughtProductsSortedDescending() {
        return orders
                .stream()
                .collect(groupingBy(toOrderMonthName,
                        collectingAndThen(
                                groupingBy(toQuantity),
                                groupedByQuantity -> groupedByQuantity
                                        .keySet()
                                        .stream()
                                        .reduce(0, Integer::sum)
                        )))
                .entrySet()
                .stream()
                .sorted((n1, n2) -> Integer.compare(n2.getValue(), n1.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (x1, x2) -> x1, LinkedHashMap::new));
    }

    /**
     *
     * @return  Most popular categories in each month
     */
    @Override
    public Map<String, Set<Category>> getMonthsWithMostPopularCategory() {
        return orders
                .stream()
                .collect(groupingBy(toOrderMonthName,
                        collectingAndThen(
                                groupingBy(orderToCategory),
                                groupedByCategory -> groupedByCategory
                                        .entrySet()
                                        .stream()
                                        .collect(groupingBy(
                                                e -> e.getValue().size(),
                                                mapping(Map.Entry::getKey, toSet())
                                        ))
                                        .entrySet()
                                        .stream()
                                        .max(Map.Entry.comparingByKey())
                                        .map(Map.Entry::getValue)
                                        .orElseThrow()
                        )));
    }

    /**
     *
     * @param createOrderDto - data from frontend form
     * @return GetOrderDto type with data about order
     */
    @Override
    public GetOrderDto addOrder(CreateOrderDto createOrderDto) {
        createOrderDtoValidator.validate(createOrderDto);
        var customerEmail = createOrderDto.email();
        var customerDataDb = createOrderDto.toCustomerDataDb();

        var timestamp = ZonedDateTime.now();
        validateDateTime(timestamp);

        if (customerRepository.findByEmail(customerEmail).isEmpty()) {
            customerRepository.save(customerDataDb);
        }

        var customer = customerRepository
                .findByEmail(customerEmail)
                .orElseThrow();

        if (customer.hasDifferentFields(customerDataDb)) {
            logger.error("Another name, surname or age is associated with this email");
            throw new OrdersServiceException("Another data is associated with given email");
        }

        var product = productRepository
                .findById(createOrderDto.productId())
                .orElseThrow(IllegalStateException::new);

        var orderToSave = OrderDataDb.builder()
                .customer(customer)
                .product(product)
                .quantity(createOrderDto.quantity())
                .orderDate(timestamp.toString())
                .build();

        orders.add(orderToSave.toOrderData().toOrder());

        return orderRepository
                .saveOrder(orderToSave)
                .map(OrderDataDb::toGetOrderDto)
                .orElseThrow(IllegalStateException::new);
    }
}

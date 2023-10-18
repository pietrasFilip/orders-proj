package com.app.persistence.config;

import com.app.persistence.data.reader.factory.FromDbToOrderWithValidator;
import com.app.persistence.data.reader.factory.FromJsonToOrderWithValidator;
import com.app.persistence.data.reader.factory.FromTxtToOrderWithValidator;
import com.app.persistence.data.reader.loader.repository.CustomerRepository;
import com.app.persistence.data.reader.loader.repository.OrderRepository;
import com.app.persistence.data.reader.loader.repository.ProductRepository;
import com.app.persistence.data.reader.loader.repository.db.impl.CustomerRepositoryImpl;
import com.app.persistence.data.reader.loader.repository.db.impl.OrderRepositoryImpl;
import com.app.persistence.data.reader.loader.repository.db.impl.ProductRepositoryImpl;
import com.app.persistence.data.reader.processor.OrderDataProcessor;
import com.app.persistence.data.reader.processor.impl.OrderDataProcessorDbImpl;
import com.app.persistence.data.reader.processor.impl.OrderDataProcessorJsonImpl;
import com.app.persistence.data.reader.processor.impl.OrderDataProcessorTxtImpl;
import com.app.persistence.data.reader.validator.CustomerDataValidator;
import com.app.persistence.data.reader.validator.OrderDataValidator;
import com.app.persistence.data.reader.validator.ProductDataValidator;
import com.app.persistence.data.reader.validator.impl.CustomerDataValidatorImpl;
import com.app.persistence.data.reader.validator.impl.OrderDataValidatorImpl;
import com.app.persistence.data.reader.validator.impl.ProductDataValidatorImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor
public class AppTestConfig {
    private final Environment environment;

    @Bean
    public Gson gson() {
        return new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    }

    @Bean
    public Jdbi jdbi() {
        var url = environment.getRequiredProperty("db.url");
        var username = environment.getRequiredProperty("db.username");
        var password = environment.getRequiredProperty("db.password");
        return Jdbi.create(url, username, password);
    }

    @Bean
    public OrderRepository orderRepository(Jdbi jdbi) {
        return new OrderRepositoryImpl(jdbi);
    }

    @Bean
    public ProductRepository productRepository(Jdbi jdbi) {
        return new ProductRepositoryImpl(jdbi);
    }

    @Bean
    public CustomerRepository customerRepository(Jdbi jdbi) {
        return new CustomerRepositoryImpl(jdbi);
    }

    @Bean
    public CustomerDataValidator customerDataValidator() {
        return new CustomerDataValidatorImpl();
    }

    @Bean
    public ProductDataValidator productDataValidator() {
        return new ProductDataValidatorImpl();
    }

    @Bean
    public OrderDataValidator orderDataValidator(CustomerDataValidator customerDataValidator,
                                                 ProductDataValidator productDataValidator) {
        return new OrderDataValidatorImpl(customerDataValidator, productDataValidator);
    }

    @Bean
    public FromDbToOrderWithValidator dataDbFactory(OrderRepository orderRepository,
                                                    OrderDataValidator orderDataValidator) {
        return new FromDbToOrderWithValidator(orderRepository, orderDataValidator);
    }

    @Bean
    public FromJsonToOrderWithValidator dataJsonFactory(Gson gson, OrderDataValidator orderDataValidator) {
        return new FromJsonToOrderWithValidator(gson, orderDataValidator);
    }

    @Bean
    public FromTxtToOrderWithValidator dataTxtFactory(OrderDataValidator orderDataValidator) {
        return new FromTxtToOrderWithValidator(orderDataValidator);
    }

    @Bean
    public OrderDataProcessor orderDataDbProcessor(FromDbToOrderWithValidator dataDbFactory) {
        return new OrderDataProcessorDbImpl(dataDbFactory);
    }

    @Bean
    public OrderDataProcessor orderDataJsonProcessor(FromJsonToOrderWithValidator dataJsonFactory) {
        return new OrderDataProcessorJsonImpl(dataJsonFactory);
    }

    @Bean
    public OrderDataProcessor orderDataTxtProcessor(FromTxtToOrderWithValidator dataTxtFactory) {
        return new OrderDataProcessorTxtImpl(dataTxtFactory);
    }
}

package com.app.service.config;

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
import com.app.persistence.model.validator.CreateOrderDtoValidator;
import com.app.persistence.model.validator.impl.CreateOrderDtoValidatorImpl;
import com.app.service.email.EmailConfiguration;
import com.app.service.email.EmailService;
import com.app.service.email.EmailServiceImpl;
import com.app.service.orders.OrdersService;
import com.app.service.orders.OrdersServiceImpl;
import com.app.service.orders.provider.OrdersProvider;
import com.app.service.orders.provider.OrdersProviderImpl;
import com.app.service.pdf.PdfService;
import com.app.service.pdf.PdfServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@RequiredArgsConstructor
public class AppTestConfig {
    private final Environment environment;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

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
    public Mailer mailer() {
        var smtpHost = environment.getRequiredProperty("simplejavamail.smtp.host");
        var smtpPort = environment.getRequiredProperty("simplejavamail.smtp.port", Integer.class);
        var smtpUsername = environment.getRequiredProperty("simplejavamail.smtp.username");
        var password = environment.getRequiredProperty("simplejavamail.smtp.password");
        return MailerBuilder
                .withSMTPServer(smtpHost, smtpPort, smtpUsername, password)
                .withTransportStrategy(TransportStrategy.SMTPS)
                .async()
                .buildMailer();
    }

    @Bean
    EmailConfiguration emailConfiguration() {
        var fromAddress = environment.getRequiredProperty("simplejavamail.defaults.from.address");
        var fromName = environment.getRequiredProperty("simplejavamail.defaults.from.name");
        return new EmailConfiguration(fromAddress, fromName);
    }

    @Bean
    public EmailService emailService(Mailer mailer, EmailConfiguration emailConfiguration) {
        return new EmailServiceImpl(mailer, emailConfiguration);
    }

    @Bean
    public PdfService pdfService() {
        return new PdfServiceImpl();
    }

    @Bean
    public CreateOrderDtoValidator createOrderDtoValidator() {
        return new CreateOrderDtoValidatorImpl();
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

    @Bean
    public OrdersProvider ordersProvider(OrderDataProcessor orderDataDbProcessor, OrderDataProcessor orderDataJsonProcessor,
                                         OrderDataProcessor orderDataTxtProcessor) {
        return new OrdersProviderImpl(orderDataDbProcessor, orderDataJsonProcessor, orderDataTxtProcessor);
    }

    @Bean
    public OrdersService ordersService(OrdersProvider ordersProvider, EmailService emailService, PdfService pdfService,
                                       OrderRepository orderRepository, CustomerRepository customerRepository,
                                       ProductRepository productRepository, CreateOrderDtoValidator createOrderDtoValidator) {
        return new OrdersServiceImpl(ordersProvider, emailService, pdfService, orderRepository, customerRepository,
                productRepository, createOrderDtoValidator);
    }

}

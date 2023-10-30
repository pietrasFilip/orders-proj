package com.app.persistence.config;

import com.app.persistence.data.reader.factory.FromDbToOrderWithValidator;
import com.app.persistence.data.reader.factory.FromJsonToOrderWithValidator;
import com.app.persistence.data.reader.factory.FromTxtToOrderWithValidator;
import com.app.persistence.data.reader.processor.OrderDataProcessor;
import com.app.persistence.data.reader.processor.impl.OrderDataProcessorDbImpl;
import com.app.persistence.data.reader.processor.impl.OrderDataProcessorJsonImpl;
import com.app.persistence.data.reader.processor.impl.OrderDataProcessorTxtImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("com.app")
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class AppConfig {
    private final Environment environment;

    @Bean
    public Gson gson() {
        return new GsonBuilder().serializeNulls().setPrettyPrinting().enableComplexMapKeySerialization().create();
    }

    @Bean
    public Jdbi jdbi() {
        var url = environment.getRequiredProperty("db.url");
        var username = environment.getRequiredProperty("db.username");
        var password = environment.getRequiredProperty("db.password");
        return Jdbi.create(url, username, password);
    }

    @Bean
    @Qualifier("orderDataDbProcessor")
    public OrderDataProcessor orderDataDbProcessor(FromDbToOrderWithValidator dataFactory) {
        return new OrderDataProcessorDbImpl(dataFactory);
    }

    @Bean
    @Qualifier("orderDataJsonProcessor")
    public OrderDataProcessor orderDataJsonProcessor(FromJsonToOrderWithValidator dataFactory) {
        return new OrderDataProcessorJsonImpl(dataFactory);
    }

    @Bean
    @Qualifier("orderDataTxtProcessor")
    public OrderDataProcessor orderDataTxtProcessor(FromTxtToOrderWithValidator dataFactory) {
        return new OrderDataProcessorTxtImpl(dataFactory);
    }
}

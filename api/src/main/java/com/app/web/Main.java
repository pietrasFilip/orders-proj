package com.app.web;

import com.app.web.config.AppConfig;
import com.app.web.routing.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static spark.Spark.initExceptionHandler;
import static spark.Spark.port;

public class Main {
    public static void main(String[] args) {
        initExceptionHandler(e -> System.out.println(e.getMessage()));
        port(8080);

        var context = new AnnotationConfigApplicationContext(AppConfig.class);

        var errorRouting = context.getBean("errorRouting", ErrorRouting.class);
        errorRouting.routes();

        var securityRouting = context.getBean("securityRouting", SecurityRouting.class);
        securityRouting.routes();

        var userRouting = context.getBean("userRouting", UserRouting.class);
        userRouting.routes();

        var ordersRouting = context.getBean("ordersRouting", OrdersRouting.class);
        ordersRouting.routes();

        var productsRouting = context.getBean("productsRouting", ProductsRouting.class);
        productsRouting.routes();
    }
}
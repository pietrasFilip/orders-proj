package com.app.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.app")
@Import({
        com.app.persistence.config.AppConfig.class,
        com.app.service.config.AppConfig.class})
public class AppConfig {
}

package com.app.service.config;

import com.app.service.email.EmailConfiguration;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
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
}

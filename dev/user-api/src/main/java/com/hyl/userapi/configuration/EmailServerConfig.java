package com.hyl.userapi.configuration;

import com.hyl.userapi.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
public class EmailServerConfig {


    // ********************************************************* Logger
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);


    // ********************************************************* Variables
    @Value("${hyl.configuration.email}")
    private String username;

    @Value("${hyl.configuration.password}")
    private String password;


    // ********************************************************* Bean
    @Bean
    public Session sessionConfig() {
        Properties props = new Properties();
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }
}

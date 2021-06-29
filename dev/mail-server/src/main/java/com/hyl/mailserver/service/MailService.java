package com.hyl.mailserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@Transactional
public class MailService {

    //************************************************** LOGGER
    Logger logger = LoggerFactory.getLogger(MailService.class);


    //************************************************** PARAMS
    private static String localUrl;

    @Value("${hyl.configuration.recipient}")
    private String recipient;


    //************************************************** BEAN
    private final JavaMailSender emailSender;


    //************************************************** SETTERS
    @Value("${hyl.url.localstorage}")
    public void setLocalUrl(String localUrl) {
        MailService.localUrl = localUrl;
    }


    //************************************************** CONSTRUCTOR
    // MailConfig.getJavaMailSender()
    @Autowired
    public MailService(@Qualifier("getJavaMailSender") JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }



    //************************************************** METHODES
    public String sendMail() throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        String htmlMsg = "<h3>Voici un titre h3</h3>"
                +"<p>Voici un paragraphe pour un <b>test</b></p>"
                +"<img src='http://www.apache.org/images/asf_logo_wide.gif'>";

        helper.setText(htmlMsg, true);

        FileSystemResource file1 = new FileSystemResource(new File(localUrl+"user1/item3/sub4/img-7.jpg"));
        FileSystemResource file2 = new FileSystemResource(new File(localUrl+"user1/item3/sub4/img-8.jpg"));
        helper.addAttachment(file1.getFilename() != null ? file1.getFilename() : "file1", file1);
        helper.addAttachment(file2.getFilename() != null ? file2.getFilename() : "file2", file2);

        helper.setTo(recipient);
        helper.setSubject("Email de test avec photos");


        this.emailSender.send(message);

        return "Email envoyé!";
    }
}

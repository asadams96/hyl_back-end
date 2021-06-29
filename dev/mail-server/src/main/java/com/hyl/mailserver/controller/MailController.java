package com.hyl.mailserver.controller;

import com.hyl.mailserver.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
public class MailController {

    //************************************************** LOGGER
    Logger logger = LoggerFactory.getLogger(MailController.class);


    //************************************************** BEAN
    private final MailService mailService;


    //************************************************** CONSTRUCTOR
    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }


    //************************************************** METHODES
    @ResponseBody
    @RequestMapping("/testmail")
    public String testMail() throws MessagingException {
        return mailService.sendMail();
    }
}

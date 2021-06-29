package com.hyl.mailserver.controller;

import com.hyl.mailserver.exception.CustomBadRequestException;
import com.hyl.mailserver.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping(path = "mail")
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

    @PostMapping("/new-password")
    public void newPassword(@RequestBody(required = false) String body) {
        JSONObject data;
        try {
            data = new JSONObject(body);
        } catch (JSONException | NullPointerException e) {
            throw new CustomBadRequestException("Le format JSON du corps de la requête est incorrect.");
        }

        String email = data.optString("email");
        String password = data.optString("password");

        if (email.isBlank())
            throw new CustomBadRequestException("L'adresse email est vide");
        else if (!email.matches("^[a-z0-9._-]{3,99}@[a-z0-9._-]{3,99}.[a-z]{2,}$"))
            throw new CustomBadRequestException("Le format de l'adresse email est incorrect");
        else if (password.isBlank())
            throw new CustomBadRequestException("Le mot de passe est vide");

        mailService.sendNewPassword(email, data.optString("civility"),
                data.optString("surname"),data.optString("name"), password);
    }
}

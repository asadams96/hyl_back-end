package com.hyl.mailserver.controller;

import com.hyl.mailserver.exception.CustomBadRequestException;
import com.hyl.mailserver.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

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
       //  return mailService.sendMail();
        return null;
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


    @PostMapping("/loan-call-back")
    public void loanCallBack(@RequestBody(required = false) String body) throws JSONException {
        JSONObject data;
        try {
            data = new JSONObject(body);
        } catch (JSONException | NullPointerException e) {
            throw new CustomBadRequestException("Le format JSON du corps de la requête est incorrect.");
        }
        String email = data.optString("email");
        String startDateStr = data.optString("startDate");
        String itemName = data.optString("itemName");
        String subItemRef = data.optString("subItemRef");
        String beneficiary = data.optString("beneficiary");
        JSONArray urlImagesArray = data.optJSONArray("urlImages");

        if (email.isBlank())
            throw new CustomBadRequestException("L'adresse email est vide");
        else if (!email.matches("^[a-z0-9._-]{3,99}@[a-z0-9._-]{3,99}.[a-z]{2,}$"))
            throw new CustomBadRequestException("Le format de l'adresse email est incorrect");
        else if (itemName.isBlank())
            throw new CustomBadRequestException("Le nom de l'objet est vide");
        else if (subItemRef.isBlank())
            throw new CustomBadRequestException("La référence du sous-objet est vide");
        else if (beneficiary.isBlank())
            throw new CustomBadRequestException("Le bénéficiaire est vide");

        Date startDate;
        try {
            startDate = Date.from(Instant.ofEpochMilli(Long.parseLong(startDateStr)));
        } catch (NullPointerException | IllegalArgumentException | DateTimeException e) {
            throw new CustomBadRequestException(e.getMessage());
        }

        List<Hashtable<String,String>> urlImages = null;
        if (urlImagesArray != null) {
            urlImages = new ArrayList<>();
            int index = urlImagesArray.length();
            for(int j = 0; j < index; j++) {
                if (!urlImagesArray.isNull(j)) {
                    JSONObject dataTable = urlImagesArray.optJSONObject(j);
                    String name = dataTable.optString("name");
                    String url = dataTable.optString("url");
                    if (!name.isBlank() && !url.isBlank()) {
                        Hashtable<String,String> table = new Hashtable<>();
                        table.put("name",name);
                        table.put("url", url);
                        if (!urlImages.contains(table)) {
                            urlImages.add(table);
                        }
                    }
                }
            }
        }

       mailService.sendLoanCallBack(email, data.optString("civility"), data.optString("surname"),
                data.optString("name"), startDate, itemName, subItemRef, beneficiary, urlImages);
    }

    @PostMapping(value = "/send-mail")
    public void sendMail(@RequestBody(required = false) String body) {
        JSONObject data;
        try {
            data = new JSONObject(body);
        } catch (JSONException | NullPointerException e) {
            throw new CustomBadRequestException("Le format JSON du corps de la requête est incorrect.");
        }

        String destinary = data.optString("destinary");
        String subject = data.optString("subject");
        String content = data.optString("content");
        String encoding = data.optString("encoding");
        boolean html = data.optBoolean("html", true);

        if (destinary.isBlank())
            throw new CustomBadRequestException("L'adresse email est vide");
        else if (!destinary.matches("^[a-z0-9._-]{3,99}@[a-z0-9._-]{3,99}.[a-z]{2,}$"))
            throw new CustomBadRequestException("Le format de l'adresse email est incorrect");
        if (subject.isBlank())
            subject = "Hyl - Courriel";
        if (content.isBlank())
            content = "Ce courriel vous a été envoyé par HYL.";
        if (encoding.isBlank())
            encoding = "utf-8";

        mailService.sendMail(destinary, subject, content, encoding, html);
    }
}

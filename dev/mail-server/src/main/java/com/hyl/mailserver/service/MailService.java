package com.hyl.mailserver.service;

import com.hyl.mailserver.exception.CustomInternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

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
    public void sendMail(String destinary, String subject, String content, String encoding, boolean html) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, encoding);
            helper.setTo(destinary);
            helper.setSubject(subject);
            helper.setText(content, html);
            this.emailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomInternalServerErrorException(e.getMessage());
        }
    }


    public void sendNewPassword(String email, String civility, String surname,
                                String name, String newPasswordNotEncrypted) {
        MimeMessage message = emailSender.createMimeMessage();
        String content = "<p>Bonjour " + defineCivility(civility) + " "
                + surname + " " + name + ",</p>"
                + "<p>Suite à votre demande, nous avons réintialisé votre mot de passe."
                + "<br />À présent pour vous connecter, vous devez utiliser le mot de passe suivant : "
                + "<strong>" + newPasswordNotEncrypted + "</strong></p>"
                + "<p>Merci de nous faire confiance et d'utiliser <strong>HYL</strong>.</p>";

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setText(content, true);
            helper.setTo(email);
            helper.setSubject("[HYL] Réintialisation du mot de passe");
            this.emailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomInternalServerErrorException(e.getMessage());
        }
    }


    public void sendLoanCallBack(String email, String civility, String surname, String name,
                                 Date startDate, String itemName, String subItemRef, String beneficiary,
                                 List<Hashtable<String, String>> urlImages) {

        MimeMessage message = emailSender.createMimeMessage();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
        int nbrDay = (int) ((new Date().getTime() -  startDate.getTime()) / (1000*60*60*24));

        String content =
                "<p>Bonjour " + defineCivility(civility) + " "
                        + surname + " " + name + ",</p>"
                        + "<p>Suite à votre demande,"
                        + "<br />Nous vous informons que le "
                        + "<strong>" + sdf.format(startDate) + "</strong> vous avez enregistré un prêt "
                        + "avec demande de rappel concernant l'objet <strong>" + itemName
                        + "</strong> ayant pour référence <strong>" + subItemRef + "</strong> à "
                        + "l'intention de <strong>" + beneficiary + "</strong>."
                        + "<br />Vous avez donc prêté votre objet depuis <strong>" + nbrDay + "</strong> jours.</p>"
                        + "<p>Merci de nous faire confiance et d'utiliser <strong>HYL</strong>.</p>";

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setText(content, true);

            if(urlImages != null && !urlImages.isEmpty()) {
                for(Hashtable<String, String> table : urlImages) {
                    File file = new File(localUrl + table.get("url"));
                    if(file.exists()) {
                        helper.addAttachment(table.get("name"), new FileDataSource(file));
                    }
                }
            }
            helper.setTo(email);
            helper.setSubject("[HYL] Rappel - Prêt en cours");
            this.emailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomInternalServerErrorException(e.getMessage());
        }
    }

    private String defineCivility(String civility) {
        if (civility != null && civility.equals("M")) return "M.";
        else if (civility != null && civility.equals("W")) return "Mme";
        else return  "";
    }
}

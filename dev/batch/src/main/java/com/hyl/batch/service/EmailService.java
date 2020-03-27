package com.hyl.batch.service;

import com.hyl.batch.model.Loan;
import com.hyl.batch.model.Picture;
import com.hyl.batch.model.SubItem;
import com.hyl.batch.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.util.Date;

@Transactional
@Service
public class EmailService {

    private static Session session;
    private static String localUrl;


    @Autowired
    public EmailService(Session session) {
        EmailService.session = session;
    }


    @Value("${hyl.url.localstorage}")
    public void setLocalUrl(String localUrl) {
        EmailService.localUrl = localUrl;
    }


    public static void sendCallBack(Loan loan) {

        User user = loan.getUser();
        SubItem subItem = loan.getSubItem();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
        int nbrDay = (int) ((new Date().getTime() -  loan.getStartDate().getTime()) / (1000*60*60*24));

        String content =
                "<p>Bonjour " + defineCivility(user.getCivility()) + " "
                + user.getSurname() + " " + user.getName() + ",</p>"
                + "<p>Suite à votre demande,"
                + "<br />Nous vous informons que le "
                + "<strong>" + sdf.format(loan.getStartDate()) + "</strong> vous avez enregistré un prêt "
                + "avec demande de rappel concernant l'objet <strong>" + subItem.getItem().getName()
                + "</strong> ayant pour référence <strong>" + subItem.getReference() + "</strong> à "
                + "l'intention de <strong>" + loan.getBeneficiary() + "</strong>."
                + "<br />Vous avez donc prêté votre objet depuis <strong>" + nbrDay + "</strong> jours.</p>"
                + "<p>Merci de nous faire confiance et d'utiliser <strong>HYL</strong>.</p>";

        try {
            Message message = new MimeMessage(session);
            message.setFrom();
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("[HYL] Rappel - Prêt en cours");

            // Création d'un corps de message + ajout du contenu texte
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html; charset=utf-8");

            //Création d'un message 'MultiPart' -> Corps + Pièce jointe
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Ajout des pièces jointes
            if (subItem.getUrlImages() != null) {
                subItem.getUrlImages().forEach(picture -> {
                    try { addAttachment(multipart, picture); } catch (MessagingException ignored) {}
                });
            }

            // Ajout du message multipart dans le message destiné à l'envoi
            message.setContent(multipart);

            // Envoi
            Transport.send(message);

        } catch (MessagingException ignored) {}
    }

    private static void addAttachment(Multipart multipart, Picture picture) throws MessagingException {
        DataSource source = new FileDataSource( localUrl + picture.getUrl() );
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(picture.getName());
        multipart.addBodyPart(messageBodyPart);
    }

    private static String defineCivility(String civility) {
        if (civility != null && civility.equals("M")) return "M.";
        else if (civility != null && civility.equals("W")) return "Mme";
        else return  "";
    }
}

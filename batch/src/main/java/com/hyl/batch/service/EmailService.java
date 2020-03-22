package com.hyl.batch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Session;

@Transactional
@Service
public class EmailService {
    private final Session session;

    @Autowired
    public EmailService(Session session) {
        this.session = session;
    }


    public void sendReminder() {
       /* String content = "<p>Bonjour " + (user.getCivility() != null ? user.getCivility()+". " : "")
                + user.getSurname() + " " + user.getName() + ",</p>"
                + "<p>Suite à votre demande, nous avons réintialisé votre mot de passe."
                + "<br />À présent pour vous connecter, vous devez utiliser le mot de passe suivant : "
                + "<strong>" + newPasswordNotEncrypted + "</strong></p>"
                + "<p>Merci de nous faire confiance et d'utiliser HYL.</p>";
        try {
            Message message = new MimeMessage(session);
            message.setFrom();
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("[HYL] Réintialisation du mot de passe");
            message.setContent(content, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new CustomInternalServerErrorException(e.getMessage());
        }*/
    }
}

package com.hyl.userapi.service;

import com.hyl.userapi.dao.UserDao;
import com.hyl.userapi.encoder.PBKDF2Encoder;
import com.hyl.userapi.exception.CustomUnauthorizedException;
import com.hyl.userapi.model.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserService {

    // ********************************************************* Bean
    private final UserDao userDao;
    private final PBKDF2Encoder passwordEncoder;
    private final HttpSession httpSession;

    // ********************************************************* Constructor
    public UserService(UserDao userDao, PBKDF2Encoder passwordEncoder, HttpSession httpSession) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.httpSession = httpSession;

        // TODO ENLEVER INSERT utilisateur exemple
        User user0 = new User();
        user0.setEmail("utilisateur@mail.fr");
        user0.setPassword(passwordEncoder.encode("password"));
        user0.setName("name");
        user0.setSurname("surname");
        user0.setPseudo("pseudo");
        userDao.save(user0);
    }


    // ********************************************************* Méthodes
    public void authenticateUser (String email, String password) {
        Optional<User> optUser = userDao.findByEmail(email);
        optUser.ifPresentOrElse(
                user -> {
                    if(!passwordEncoder.matches(password, user.getPassword())) {
                        throw new CustomUnauthorizedException("Couple email/password incorrect");
                    } else {
                        httpSession.setAttribute("user"+user.getId(), user);
                    }
                },
                () -> {
                    throw new CustomUnauthorizedException("Utilisateur inconnu");
                }
        );
    }
}

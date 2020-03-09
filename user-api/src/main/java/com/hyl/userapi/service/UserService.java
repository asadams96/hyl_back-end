package com.hyl.userapi.service;

import com.hyl.userapi.dao.UserDao;
import com.hyl.userapi.encoder.PBKDF2Encoder;
import com.hyl.userapi.exception.CustomUnauthorizedException;
import com.hyl.userapi.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserService {

    // ********************************************************* Logger
    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    // ********************************************************* Bean
    private final UserDao userDao;
    private final PBKDF2Encoder passwordEncoder;
    private final HttpSession httpSession;

    // ********************************************************* Constructor
    public UserService(UserDao userDao, PBKDF2Encoder passwordEncoder, HttpSession httpSession) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.httpSession = httpSession;
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

    public void registerUser(User user) {
        user.setCellphone( "+33" + user.getCellphone().substring(1) );
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }
}

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
    public String authenticateUser (String email, String password) {
        Optional<User> optUser = userDao.findByEmail(email);

        if (optUser.isPresent()) {
            User user = optUser.get();
            if(!passwordEncoder.matches(password, user.getPassword())) {
                throw new CustomUnauthorizedException("Couple email/password incorrect");
            } else {
                httpSession.setAttribute("user"+user.getId(), user);
                return String.valueOf(user.getId());
            }
        } else {
            throw new CustomUnauthorizedException("Utilisateur inconnu");
        }
    }

    public void registerUser(User user) {
        if (user.getCellphone() != null && !user.getCellphone().isEmpty()) {
            user.setCellphone( "+33" + user.getCellphone().substring(1) );
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public void disconnectUser(long idUser) {
        httpSession.removeAttribute("user"+idUser);
    }
}

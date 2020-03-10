package com.hyl.userapi.service;

import com.hyl.userapi.dao.UserDao;
import com.hyl.userapi.encoder.PBKDF2Encoder;
import com.hyl.userapi.exception.CustomUnauthorizedException;
import com.hyl.userapi.model.User;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    // ********************************************************* Logger
    private final Logger logger = LoggerFactory.getLogger(UserService.class);


    // ********************************************************* Bean
    private final UserDao userDao;
    private final PBKDF2Encoder passwordEncoder;
    private final HttpSession httpSession;
    private final EmailService emailService;

    // ********************************************************* Constructor
    public UserService(UserDao userDao, PBKDF2Encoder passwordEncoder, HttpSession httpSession, EmailService emailService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.httpSession = httpSession;
        this.emailService = emailService;
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
            throw new CustomUnauthorizedException("Aucun utilisateur n'est associé à "+email);
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

    public void forgotPasswordUser(String email) {
        userDao.findByEmail(email).ifPresentOrElse(user -> {
            String password = generatePassword();
            user.setPassword(passwordEncoder.encode(password));
            userDao.save(user);
            emailService.sendNewPassword(user, password);

        }, () -> {
            throw new CustomUnauthorizedException("Aucun utilisateur n'est associé à "+email);
        });

    }

    public Boolean checkAtomicEmail(String email) {
        return userDao.findByEmail(email).isEmpty();
    }

    private String generatePassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters).concat(numbers).concat(specialChar).concat(totalChars);
        List<Character> pwdChars = combinedChars.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return  pwdChars.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).toString();
    }
}

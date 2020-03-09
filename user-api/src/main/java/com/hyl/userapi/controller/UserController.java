package com.hyl.userapi.controller;

import com.hyl.userapi.exception.CustomBadRequestException;
import com.hyl.userapi.model.User;
import com.hyl.userapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "user")
public class UserController {

    // ********************************************************* Logger
    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    // ********************************************************* Bean
    private final UserService userService;


    // ********************************************************* Constructor
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    // ********************************************************* GET
    @GetMapping(path = "/{id}")
    public void getUser() {
        // TODO
    }

    @GetMapping(path = "/check-email")
    public void checkEmail () {
        // TODO
    }


    // ********************************************************* POST
    @PostMapping(path = "/signin")
    public String signin (@Validated(User.AuthenticateValidation.class) @RequestBody User user) {
        return userService.authenticateUser(user.getEmail(), user.getPassword());
    }

    @PostMapping(path = "/signup")
    public void signup (@Validated(User.RegisterValidation.class) @RequestBody User user) {
        userService.registerUser(user);
    }

    @PostMapping(path = "/signout")
    public void signout (@Autowired HttpServletRequest request) {
        String idUserStr = request.getHeader("idUser");
        if ( idUserStr == null ) {
            throw new CustomBadRequestException("Aucun utilisateur n'est spécifié dans le header 'idUser' de la requête.");
        }

        long idUser;
        try {
            idUser = Long.parseLong(idUserStr);
        } catch (NumberFormatException e) {
            throw new CustomBadRequestException("L'id de l'utilisateur à déconnecter doit être un nombre.");
        }

        userService.disconnectUser(idUser);
    }

    @PostMapping(path = "/forgot-password")
    public void forgotPassword () {
        // TODO
    }


    // ********************************************************* PATCH
    @PatchMapping(path = "/{id}")
    public void updateUser () {
        // TODO
    }
}

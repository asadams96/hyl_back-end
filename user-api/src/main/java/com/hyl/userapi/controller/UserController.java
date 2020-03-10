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
    @GetMapping(path = "/get-user")
    public User getUser(@Autowired HttpServletRequest request) {
        return userService.getUserById(extractIdUserFromHeader(request));
    }

    @GetMapping(path = "/check-email")
    public Boolean checkEmail (@RequestParam String email) {
        return !userService.checkAtomicEmail(email);
    }

    @GetMapping(path = "/check-cellphone")
    public Boolean checkCellphone (@RequestParam String cellphone) {
        return !userService.checkAtomicCellphone(cellphone);
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
        userService.disconnectUser(extractIdUserFromHeader(request));
    }

    @PostMapping(path = "/forgot-password")
    public void forgotPassword (@RequestBody String email) {
        userService.forgotPasswordUser(email);
    }


    // ********************************************************* PATCH
    @PatchMapping(path = "/patch-user")
    public void updateUser (@Autowired HttpServletRequest request, @RequestBody User user) {
        userService.updateUser(extractIdUserFromHeader(request), user);
    }


    // ********************************************************* SHARE
    private long extractIdUserFromHeader (HttpServletRequest request) {
        String idUserStr = request.getHeader("idUser");
        if ( idUserStr == null ) {
            throw new CustomBadRequestException("Aucun utilisateur n'est spécifié dans le header 'idUser' de la requête.");
        }

        try {
            return Long.parseLong(idUserStr);
        } catch (NumberFormatException e) {
            throw new CustomBadRequestException("L'id de l'utilisateur doit être un nombre.");
        }
    }
}

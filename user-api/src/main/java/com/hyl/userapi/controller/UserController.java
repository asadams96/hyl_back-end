package com.hyl.userapi.controller;

import com.hyl.userapi.dao.UserDao;
import com.hyl.userapi.encoder.PBKDF2Encoder;
import com.hyl.userapi.exception.CustomBadRequestException;
import com.hyl.userapi.exception.CustomUnauthorizedException;
import com.hyl.userapi.model.User;
import com.hyl.userapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping(path = "user")
public class UserController {

    // ********************************************************* Logger
    Logger logger = LoggerFactory.getLogger(UserController.class);


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
    public void signin (@RequestBody HashMap<String, String> hashMap) {
       String email = hashMap.get("email");
       String password = hashMap.get("password");

       if ( email == null || password == null || email.isBlank() || password.isBlank() ) {
           throw new CustomBadRequestException("Les champs 'email' et 'password' ne doivent pas être vide");
       } else {
          userService.authenticateUser(email, password);
       }
    }

    @PostMapping(path = "/signup")
    public void signup () {
        // TODO
    }

    @PostMapping(path = "/signout")
    public void signout () {
        // TODO
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

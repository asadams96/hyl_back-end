package com.hyl.userapi.controller;

import com.hyl.userapi.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "user")
public class UserController {

    // ********************************************************* Logger
    Logger logger = LoggerFactory.getLogger(UserController.class);


    // ********************************************************* Bean
    private final UserDao userDao;


    // ********************************************************* Constructor
    @Autowired
    public UserController(UserDao userDao) {
        this.userDao = userDao;
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
    public void signin () {
        // TODO
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

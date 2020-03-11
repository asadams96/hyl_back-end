package com.hyl.loanapi.controller;

import com.hyl.loanapi.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "loan")
public class LoanController {

    // ********************************************************* logger
    private final Logger logger = LoggerFactory.getLogger(LoanController.class);


    // ********************************************************* Bean
    private final LoanService loanService;


    // ********************************************************* Constructeur
    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }


    // ********************************************************* GET
    @GetMapping("/in-progress")
    public void getLoanInProgress() {
        // TODO
    }

    @GetMapping("/terminated")
    public void getLoanTerminated() {
        // TODO
    }


    // ********************************************************* POST
    @PostMapping("/add-loan")
    public void addLoan() {
        // TODO
    }

    @PostMapping("/close-loans")
    public void closeLoan() {
        // TODO
    }


    // ********************************************************* DELETE
    @DeleteMapping("/delete-loans")
    public void deleteLoan() {
        // TODO
    }
}

package com.hyl.batch.controller;

import com.hyl.batch.model.Loan;
import com.hyl.batch.service.LoanService;
import com.hyl.batch.service.MailService;
import com.hyl.batch.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;

@Controller
public class BatchController implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(BatchController.class);

    public final UserService userService;

    @Autowired
    public BatchController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        // Récupération des prêts en cours où un rappel est prévu 'aujourd'hui'
        List<Loan> loans = LoanService.getLoansToCallBack();

        // Récupération du token admin (pour passer l'authentification ayant lieu lors du contact avec le serveur-mail)
        HashMap<String,String> signInAdminMap = userService.signInAdmin();

        // Envoi d'un email à l'utilisateur
        loans.forEach(loan -> MailService.sendCallBack(loan, signInAdminMap));

    }
}

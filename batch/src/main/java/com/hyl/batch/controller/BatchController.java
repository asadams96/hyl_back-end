package com.hyl.batch.controller;

import com.hyl.batch.model.Loan;
import com.hyl.batch.service.EmailService;
import com.hyl.batch.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BatchController implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(BatchController.class);


    @Override
    public void run(String... args) throws Exception {

        // Récupération des prêts en cours où un rappel est prévu 'aujourd'hui'
        List<Loan> loans = LoanService.getLoansToCallBack();

        // Envoi d'un email à l'utilisateur
        loans.forEach(EmailService::sendCallBack);
    }
}

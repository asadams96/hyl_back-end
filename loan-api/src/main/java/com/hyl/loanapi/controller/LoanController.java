package com.hyl.loanapi.controller;

import com.hyl.loanapi.exception.CustomBadRequestException;
import com.hyl.loanapi.model.Loan;
import com.hyl.loanapi.model.State;
import com.hyl.loanapi.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public List<Loan> getLoansInProgress(@Autowired HttpServletRequest request) {
        return loanService.getLoans(this.extractIdUserFromHeader(request), State.IN_PROGRESS);
    }

    @GetMapping("/terminated")
    public List<Loan> getLoansTerminated(@Autowired HttpServletRequest request) {
        return loanService.getLoans(this.extractIdUserFromHeader(request), State.TERMINATED);
    }


    // ********************************************************* POST
    @PostMapping("/add-loan")
    public Loan addLoan(@Autowired HttpServletRequest request,
                        @Validated(Loan.AddValidation.class) @RequestBody Loan loan) {
        return loanService.addLoan(this.extractIdUserFromHeader(request), loan);
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
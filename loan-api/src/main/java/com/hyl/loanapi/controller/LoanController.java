package com.hyl.loanapi.controller;

import com.hyl.loanapi.exception.CustomBadRequestException;
import com.hyl.loanapi.exception.CustomNotFoundException;
import com.hyl.loanapi.model.Loan;
import com.hyl.loanapi.model.State;
import com.hyl.loanapi.model.validation.ValidCloseLoansListValidation;
import com.hyl.loanapi.service.LoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return loanService.getLoans(extractIdUserFromHeader(request), State.IN_PROGRESS);
    }

    @GetMapping("/terminated")
    public List<Loan> getLoansTerminated(@Autowired HttpServletRequest request) {
        return loanService.getLoans(extractIdUserFromHeader(request), State.TERMINATED);
    }


    // ********************************************************* POST
    @PostMapping("/add-loan")
    public Loan addLoan(@Autowired HttpServletRequest request,
                        @Validated(Loan.AddValidation.class) @RequestBody Loan loan) {
        return loanService.addLoan(extractIdUserFromHeader(request), loan);
    }

    @PostMapping("/close-loans")
    public void closeLoan(@Validated(Loan.CloseValidation.class) @RequestBody ValidCloseLoansListValidation loans) {
        // Todo S'il existe, le commentaire du prêt doit être enregistré auprès de item-api lorsqu'il sera opérationnel
        loans.forEach(loanService::closeLoan);
    }


    // ********************************************************* DELETE
    @DeleteMapping("/delete-loans")
    public void deleteLoan(@Validated(Loan.DeleteValidation.class) @RequestBody ValidCloseLoansListValidation loans) {
       loans.forEach(loanService::deleteLoan);
    }


    // ********************************************************* SHARE
    @ExceptionHandler(value = {CustomBadRequestException.class, CustomNotFoundException.class})
    public ResponseEntity<?> handle(RuntimeException exception) {
        if (exception.getCause() != null) exception = (RuntimeException) exception.getCause();

        if (exception.getClass().equals(CustomBadRequestException.class)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body((exception.getCause() != null &&  exception.getCause().getMessage() != null
                            ? exception.getCause().getMessage() : exception.getMessage()));
        } else if (exception.getClass().equals(CustomNotFoundException.class)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body((exception.getCause() != null &&  exception.getCause().getMessage() != null
                            ? exception.getCause().getMessage() : exception.getMessage()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((exception.getCause() != null &&  exception.getCause().getMessage() != null
                            ? exception.getCause().getMessage() : exception.getMessage()));
        }
    }

    public static long extractIdUserFromHeader (HttpServletRequest request) {
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

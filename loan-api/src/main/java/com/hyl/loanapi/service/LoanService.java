package com.hyl.loanapi.service;

import com.hyl.loanapi.dao.LoanDao;
import com.hyl.loanapi.model.Loan;
import com.hyl.loanapi.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    // ********************************************************* Bean
    private final LoanDao loanDao;


    // ********************************************************* Constructeur
    @Autowired
    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }


    // ********************************************************* Métohdes
    public List<Loan> getLoans(long idOwner, State state) {
        switch (state) {
            case IN_PROGRESS:
                return loanDao.findAllByIdOwnerAndEndDateIsNull(idOwner);
            case TERMINATED:
                return loanDao.findAllByIdOwnerAndEndDateIsNotNull(idOwner);
            default:
                return new ArrayList<>();
        }
    }
}

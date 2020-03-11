package com.hyl.loanapi.service;

import com.hyl.loanapi.dao.LoanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    // ********************************************************* Bean
    private final LoanDao loanDao;


    // ********************************************************* Constructeur
    @Autowired
    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }
}

package com.hyl.batch.service;

import com.hyl.batch.dao.LoanDao;
import com.hyl.batch.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LoanService {

    private static LoanDao loanDao;

    @Autowired
    public LoanService(LoanDao loanDao) {
        LoanService.loanDao = loanDao;
    }

    public static List<Loan> getAll() {
        return loanDao.findAll();
    }

}

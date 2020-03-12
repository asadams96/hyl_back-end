package com.hyl.loanapi.service;

import com.hyl.loanapi.dao.LoanDao;
import com.hyl.loanapi.exception.CustomBadRequestException;
import com.hyl.loanapi.exception.CustomNotFoundException;
import com.hyl.loanapi.model.Loan;
import com.hyl.loanapi.model.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    // ********************************************************* logger
    private final Logger logger = LoggerFactory.getLogger(LoanService.class);


    // ********************************************************* Bean
    private final LoanDao loanDao;


    // ********************************************************* Constructeur
    @Autowired
    public LoanService(LoanDao loanDao) {
        this.loanDao = loanDao;
    }


    // ********************************************************* Métohdes
    public Loan getLoan(Long idLoan) {
        if (idLoan == null) throw new CustomBadRequestException("L'id du prêt recherché ne peut pas être null");
        Optional<Loan> optLoan = loanDao.findById(idLoan);
        if (optLoan.isPresent()) return optLoan.get();
        else throw new CustomNotFoundException("Le prêt "+idLoan+" n'existe pas");

    }

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

    public Loan addLoan(long idOwner, Loan loan) {
        loan.setIdOwner(idOwner);
        return this.loanDao.save(loan);
    }

    public void closeLoan(Loan pLoan) {
        Loan loan = this.getLoan(pLoan.getId());
        loan.setEndDate(pLoan.getEndDate());
        loanDao.save(loan);

    }

    public void deleteLoan(Loan loan) {
        loanDao.delete(loan);
    }
}

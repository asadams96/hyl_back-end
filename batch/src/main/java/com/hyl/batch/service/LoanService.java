package com.hyl.batch.service;

import com.hyl.batch.dao.LoanDao;
import com.hyl.batch.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class LoanService {

    private static LoanDao loanDao;

    @Autowired
    public LoanService(LoanDao loanDao) {
        LoanService.loanDao = loanDao;
    }


    public static List<Loan> getLoansToCallBack() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Copenhagen"));

        Date intervalMin = new GregorianCalendar( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),0,0,0).getTime();

        Date intervalMax = new GregorianCalendar( cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),23,59,59).getTime();

        List<Loan> loans =  loanDao.findAllByReminderBetweenAndEndDateIsNull(intervalMin, intervalMax);
        List<Loan> copyLoans = new ArrayList<>(loans);
        loans.clear();
        copyLoans.forEach(loan -> {
            try {
                loan = buildCompletelyLoan(loan);
                loans.add(loan);
            } catch (RuntimeException ignored) {}
        });
        return loans;
    }

    private static Loan buildCompletelyLoan(Loan loan) {

        if (loan == null || loan.getIdOwner() == null || loan.getIdOwner() == 0L
                    || loan.getReference() == null || loan.getReference().isBlank()) {

            throw new RuntimeException("Une erreur s'est produite");

        } else {
            loan.setUser(UserService.getUserById(loan.getIdOwner()));
            loan.setSubItem(ItemService.getSubItemByReference(loan.getReference()));
            return loan;
        }
    }

}

package com.hyl.loanapi.model.constraint.validator;

import com.hyl.loanapi.model.Loan;
import com.hyl.loanapi.model.constraint.EndDateConstraint;
import com.hyl.loanapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EndDateValidator implements ConstraintValidator<EndDateConstraint, Loan> {

    @Autowired
    LoanService loanService;

    @Override
    public void initialize(EndDateConstraint endDateConstraint) {

    }

    @Override
    public boolean isValid(Loan pLoan, ConstraintValidatorContext context) {
        Loan loan = loanService.getLoan(pLoan.getId());
        return loan.getStartDate().before(pLoan.getEndDate());
    }
}

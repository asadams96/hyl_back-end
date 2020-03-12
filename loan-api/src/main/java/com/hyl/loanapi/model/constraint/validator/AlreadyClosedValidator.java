package com.hyl.loanapi.model.constraint.validator;

import com.hyl.loanapi.model.Loan;
import com.hyl.loanapi.model.constraint.AlreadyClosedConstraint;
import com.hyl.loanapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AlreadyClosedValidator implements ConstraintValidator<AlreadyClosedConstraint, Loan> {

    @Autowired
    LoanService loanService;

    @Override
    public void initialize(AlreadyClosedConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(Loan loan, ConstraintValidatorContext context) {
       return loanService.getLoan(loan.getId()).getEndDate() == null;
    }
}

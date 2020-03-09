package com.hyl.userapi.model.constraint.validator;

import com.hyl.userapi.dao.UserDao;
import com.hyl.userapi.model.constraint.AtomicEmailConstraint;
import com.hyl.userapi.model.constraint.CellphoneConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtomicEmailValidator implements ConstraintValidator<AtomicEmailConstraint, String> {

    @Autowired
    UserDao userDao;

    @Override
    public void initialize(AtomicEmailConstraint atomicEmailConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        return userDao.findByEmail(field).isEmpty();
    }
}

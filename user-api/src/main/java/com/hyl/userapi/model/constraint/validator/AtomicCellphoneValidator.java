package com.hyl.userapi.model.constraint.validator;

import com.hyl.userapi.dao.UserDao;
import com.hyl.userapi.model.constraint.AtomicCellphoneConstraint;
import com.hyl.userapi.model.constraint.AtomicEmailConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtomicCellphoneValidator implements ConstraintValidator<AtomicCellphoneConstraint, String> {

    @Autowired
    UserDao userDao;

    @Override
    public void initialize(AtomicCellphoneConstraint atomicCellphoneConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        if (field != null && !field.isEmpty()) {
            field = "+33" + field.substring(1);
            return userDao.findByCellphone(field).isEmpty();
        } else return true;
    }
}

package com.hyl.userapi.model.constraint.validator;

import com.hyl.userapi.model.constraint.AtomicEmailConstraint;
import com.hyl.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtomicEmailValidator implements ConstraintValidator<AtomicEmailConstraint, String> {

    @Autowired
    UserService userService;

    @Override
    public void initialize(AtomicEmailConstraint atomicEmailConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        return userService.checkAtomicEmail(field);
    }
}

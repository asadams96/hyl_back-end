package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.model.constraint.AtomicSubItemRefConstraint;
import com.hyl.itemapi.service.SubItemService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtomicSubItemRefValidator implements ConstraintValidator<AtomicSubItemRefConstraint, String> {

    @Override
    public void initialize(AtomicSubItemRefConstraint atomicSubItemRefConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        return SubItemService.checkAtomicRef(field);
    }
}

package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.model.constraint.AtomicItemNameConstraint;
import com.hyl.itemapi.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtomicItemNameValidator implements ConstraintValidator<AtomicItemNameConstraint, String> {

    @Autowired
    ItemService itemService;

    @Override
    public void initialize(AtomicItemNameConstraint atomicItemNameConstraint) {
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {
        return itemService.checkAtomicName(field);
    }
}

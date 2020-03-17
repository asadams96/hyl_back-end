package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.controller.ItemController;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.constraint.IdOwnerConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdOwnerValidator implements ConstraintValidator<IdOwnerConstraint, Item> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(IdOwnerConstraint idOwnerConstraint) {

    }

    @Override
    public boolean isValid(Item item, ConstraintValidatorContext context) {
        return ItemController.extractIdUserFromHeader(request) == item.getIdUser();
    }
}

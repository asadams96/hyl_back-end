package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.controller.ItemController;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.constraint.IdOwnerConstraint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdOwnerValidator implements ConstraintValidator<IdOwnerConstraint, Object> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void initialize(IdOwnerConstraint idOwnerConstraint) {

    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        Item item;
        if (object.getClass().equals(SubItem.class)) {
            item = ((SubItem) object).getItem();
        } else if (object.getClass().equals(Item.class)) {
            item = (Item) object;
        } else {
            return false;
        }
        return ItemController.extractIdUserFromHeader(request) == item.getIdUser();
    }
}

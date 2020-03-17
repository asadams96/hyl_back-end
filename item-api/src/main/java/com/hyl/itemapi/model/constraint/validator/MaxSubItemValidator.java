package com.hyl.itemapi.model.constraint.validator;

import com.hyl.itemapi.controller.ItemController;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.constraint.MaxSubItemConstraint;
import com.hyl.itemapi.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class MaxSubItemValidator implements ConstraintValidator<MaxSubItemConstraint, SubItem> {

    @Autowired
    private HttpServletRequest request;

    @Value("${hyl.constraint.subitem.max}")
    private String max;

    @Override
    public void initialize(MaxSubItemConstraint maxSubItemConstraint) {

    }

    @Override
    public boolean isValid(SubItem pSubItem, ConstraintValidatorContext context) {
        int maxSubItemAuthorized = Integer.parseInt(this.max);
        int count = 0;
        List<Item> items = ItemService.getItemsByIdUser( ItemController.extractIdUserFromHeader(request) );
        for (Item item : items) {
            for (SubItem subItem : item.getSubItems()) {
                count++;
            }
        }
        return count < maxSubItemAuthorized;
    }
}

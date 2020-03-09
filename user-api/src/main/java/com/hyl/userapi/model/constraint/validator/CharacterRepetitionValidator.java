package com.hyl.userapi.model.constraint.validator;

import com.hyl.userapi.model.constraint.CellphoneConstraint;
import com.hyl.userapi.model.constraint.CharacterRepetitionConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CharacterRepetitionValidator implements ConstraintValidator<CharacterRepetitionConstraint, String> {

    private int value;

    @Override
    public void initialize(CharacterRepetitionConstraint characterRepetitionConstraint) {
        this.value = characterRepetitionConstraint.value();
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {

        field = field.toLowerCase();
        String extraction;
        boolean repetition;
        for (int i = 0; i < field.length(); i++) {
            if ( (i+value) <= field.length()) {
                repetition = true;
                extraction = field.substring(i, i + value);

                for (int j = 1; j < value; j++) {
                    if ( !extraction.substring(0, 1).equals( extraction.substring(j, j + 1) ) ) {
                        repetition = false;
                        break;
                    }
                }
                if ( repetition ) return false;
            }
        }
        return true;
    }
}

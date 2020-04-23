package ogorek.wojciech.persistance.validator.impl;

import ogorek.wojciech.persistance.validator.generic.AbstractValidator;

import java.util.Map;

public class ModelValidator extends AbstractValidator<String> {
    public Map<String, String> validate (String carModel){

        errors.clear();

        if(carModel == null){
            errors.put("random carModel", " is null");
            return errors;
        }
        if(!isRandomCarModelValid(carModel)){
            errors.put("random carModel", " name is invalid");
            return errors;
        }

        return errors;

    }

    private boolean isRandomCarModelValid(String carModel){
        return carModel != null && carModel.matches("[A-Z]+");
    }
}

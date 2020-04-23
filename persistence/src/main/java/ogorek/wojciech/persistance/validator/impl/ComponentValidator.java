package ogorek.wojciech.persistance.validator.impl;

import ogorek.wojciech.persistance.validator.generic.AbstractValidator;

import java.util.Map;

public class ComponentValidator extends AbstractValidator<String> {
    public Map<String, String> validate(String component) {

        errors.clear();

        if (component == null) {
            errors.put("component", "string is null");
            return errors;

        }
        if(!isRandomComponentValid(component)){
            errors.put("component", "component is invalid");
        }
        return errors;
    }

    private boolean isRandomComponentValid(String component){
        return component != null && component.matches("[A-Z\\s]+");
    }
}

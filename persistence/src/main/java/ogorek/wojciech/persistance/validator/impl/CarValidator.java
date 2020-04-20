package ogorek.wojciech.persistance.validator.impl;

import ogorek.wojciech.persistance.model.Car;
import ogorek.wojciech.persistance.model.enums.Color;
import ogorek.wojciech.persistance.validator.generic.AbstractValidator;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class CarValidator extends AbstractValidator<Car> {

    @Override
    public Map<String, String> validate(Car car) {

        errors.clear();

        if (car == null) {
            errors.put("car", "object is null");
            return errors;
        }

        if (!isCarModelValid(car.getModel())) {
            errors.put("car model", "model should contain only upper case and white spaces: " + car.getModel());
        }

        if (!isCarColorValid(car.getColor())) {
            errors.put("car color", "color object cannot be null: " + car.getColor());
        }

        if (!isCarMileageValid(car.getMileage())) {
            errors.put("car mileage", "car mileage should have positive value: " + car.getMileage());
        }

        if (!isCarPriceValid(car.getPrice())) {
            errors.put("car price", "car price should have positive value: " + car.getPrice());
        }

        if (!areCarComponentsValid(car.getComponents())) {
            errors.put("car components", "car components should contain upper case or white spaces: " + car.getComponents());
        }

        return errors;
    }

    private boolean isCarModelValid(String carModel) {
        return carModel != null && carModel.matches("[A-Z\\s]+");
    }

    private boolean isCarColorValid(Color carColor) {
        return carColor != null;
    }

    private boolean isCarMileageValid(double carMileage) {
        return carMileage > 0;
    }

    private boolean isCarPriceValid(BigDecimal carPrice) {
        return carPrice != null && carPrice.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean areCarComponentsValid(Set<String> carComponents) {
        return carComponents != null && carComponents.stream().allMatch(component -> component.matches("[A-Z\\s]+"));
    }
}

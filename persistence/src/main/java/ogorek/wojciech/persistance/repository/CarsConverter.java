package ogorek.wojciech.persistance.repository;

import ogorek.wojciech.persistance.model.Car;

import java.util.List;

public class CarsConverter extends JsonConverter<List<Car>> {

    public CarsConverter(String jsonFilename){
        super(jsonFilename);
    }
}

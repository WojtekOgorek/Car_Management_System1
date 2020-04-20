package ogorek.wojciech.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ogorek.wojciech.persistance.exception.JsonAppException;
import ogorek.wojciech.persistance.model.Car;
import ogorek.wojciech.persistance.repository.CarsConverter;
import ogorek.wojciech.persistance.validator.impl.CarValidator;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CarsService {

    private final Set<Car> cars;

    public CarsService(String jsonFilename){cars = readCarsFromJsonfile(jsonFilename);}

    private Set<Car> readCarsFromJsonfile(String jsonFilename){

        var carValidator = new CarValidator();
        var counter = new AtomicInteger(1);

        return new CarsConverter(jsonFilename)
                .fromJson()
                .orElseThrow(() -> new JsonAppException("cars service - cannot read data from jsonfile"))
                .stream()
                .filter(car -> {
                    var errors = carValidator.validate(car);
                            if(carValidator.hasErrors()){
                                System.out.println("\n---------- validation errors for car nr." + counter.get() + " --------------");
                                errors.forEach((k,v) -> System.out.println(k+ ": " + v));
                                System.out.println("\n\n");
                            }
                            counter.incrementAndGet();
                            return !carValidator.hasErrors();
                }).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return cars
                .stream()
                .map(gson::toJson)
                .collect(Collectors.joining("\n"));
    }
}

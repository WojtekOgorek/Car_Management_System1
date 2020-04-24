package ogorek.wojciech.service;

import ogorek.wojciech.persistance.model.Car;
import ogorek.wojciech.service.utils.UserDataService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DataGenerator {


    private Car car;
    private RandomDataService randomDataService = new RandomDataService();


    private Car generateRandomCar(){

        car = Car.builder()
                .model(randomDataService.getRandomModelFromCollection())
                .price(randomDataService.randomPrice())
                .mileage(randomDataService.randomMileage())
                .color(randomDataService.randomColor())
                .components(randomDataService.getRandomComponentsFromCollection())
                .build();

        System.out.println(car);

        return car;
    }

    private Car generateUserCar(){

        car = Car.builder()
                .model(UserDataService.getString("Type Model"))
                .price(UserDataService.getBigDecimal("Type price"))
                .mileage(UserDataService.getDouble("Type mileage"))
                .components(typeComponent())
                .color(UserDataService.getColor("Type Color"))
                .build();

        return car;
    }

    private Set<String> typeComponent(){
        boolean isTrue;
        Set<String> components = new LinkedHashSet<>();
        do{
            String component = UserDataService.getString("Type component");

            isTrue = UserDataService.getBoolean("Wanna add more components ? y/n");

            components.add(component);

        }while(isTrue);

        return components;
    }

    public List<Car> createACar() {
        Car car;
        boolean nextCar;
        List<Car> listOfGeneratedCars = new ArrayList<>();
        do {
            boolean isTrue = UserDataService.getBoolean("Do you want randomly generate a car ? y/n ?");
            if (!isTrue) {
                car = generateUserCar();
            } else
                car = generateRandomCar();
            nextCar = UserDataService.getBoolean("wanna add next car ? y/n");
            listOfGeneratedCars.add(car);
        } while (nextCar);

        return listOfGeneratedCars;
    }
}

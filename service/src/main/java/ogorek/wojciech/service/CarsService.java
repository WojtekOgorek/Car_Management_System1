package ogorek.wojciech.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ogorek.wojciech.persistance.data.Statistic;
import ogorek.wojciech.persistance.data.Statistics;
import ogorek.wojciech.persistance.exception.AppException;
import ogorek.wojciech.persistance.exception.JsonAppException;
import ogorek.wojciech.persistance.model.Car;
import ogorek.wojciech.persistance.model.enums.Color;
import ogorek.wojciech.persistance.model.enums.SortItem;
import ogorek.wojciech.persistance.repository.CarsConverter;
import ogorek.wojciech.persistance.validator.impl.CarValidator;
import org.eclipse.collections.impl.collector.BigDecimalSummaryStatistics;
import org.eclipse.collections.impl.collector.Collectors2;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarsService {

    private final Set<Car> cars;

    public CarsService(String jsonFilename) {
        cars = readCarsFromJsonfile(jsonFilename);
    }

    private Set<Car> readCarsFromJsonfile(String jsonFilename) {

        var carValidator = new CarValidator();
        var counter = new AtomicInteger(1);

        return new CarsConverter(jsonFilename)
                .fromJson()
                .orElseThrow(() -> new JsonAppException("cars service - cannot read data from jsonfile"))
                .stream()
                .filter(car -> {
                    var errors = carValidator.validate(car);
                    if (carValidator.hasErrors()) {
                        System.out.println("\n---------- validation errors for car nr." + counter.get() + " --------------");
                        errors.forEach((k, v) -> System.out.println(k + ": " + v));
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

    //method 1. Sort Car by COLOR, MILEAGE, MODEL -> descending or ascending

    public List<Car> sort(SortItem sortItem, boolean descending) {

        if (sortItem == null) {
            throw new AppException("sort item object is null");
        }

        Stream<Car> carStream = switch (sortItem) {
            case COLOR -> cars.stream().sorted(Comparator.comparing(Car::getColor));
            case MILEAGE -> cars.stream().sorted(Comparator.comparing(Car::getMileage));
            case MODEL -> cars.stream().sorted(Comparator.comparing(Car::getModel));
            default -> cars.stream().sorted(Comparator.comparing(Car::getPrice));
        };

        List<Car> sortedCars = carStream.collect(Collectors.toList());
        if (descending) {
            Collections.reverse(sortedCars);
        }
        return sortedCars;
    }

    //method 2. Get all cars with mileage grater than given argument.

    public List<Car> getAllWithMileageGreaterThan(double mileage) {

        if (mileage < 0) {
            throw new AppException("mileage value is not correct " + mileage);
        }

        return cars
                .stream()
                .filter(car -> car.getMileage() > mileage)
                .collect(Collectors.toList());
    }

    //method 3. Count cars by colors.

    public Map<Color, Long> countCarsByColors() {
        return
                cars
                        .stream()
                        .collect(Collectors.groupingBy(Car::getColor, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::max, LinkedHashMap::new));

    }

    //method 4. Summarizing price and mileage statisctics

    public Statistics summarizeMileageAndPrice() {

        DoubleSummaryStatistics mileageStatistics = cars.stream().collect(Collectors.summarizingDouble(mileage -> mileage.getMileage()));
        BigDecimalSummaryStatistics priceStatistics = cars.stream().collect(Collectors2.summarizingBigDecimal(price -> price.getPrice()));

        Statistic<Double> mileageStatisctic = Statistic.<Double>builder()
                .min(mileageStatistics.getMin())
                .max(mileageStatistics.getMax())
                .avg(mileageStatistics.getAverage())
                .build();

        Statistic<BigDecimal> priceStatistic = Statistic.<BigDecimal>builder()
                .min(priceStatistics.getMin())
                .max(priceStatistics.getMax())
                .avg(priceStatistics.getAverage())
                .build();


        return Statistics.builder()
                .mileageStatistics(mileageStatisctic)
                .priceStatistics(priceStatistic)
                .build();
    }

    //method 5. Sorting car components

    public List<Car> sortCarComponents() {
        return cars
                .stream()
                .peek(car -> car.setComponents(car.getComponents()
                        .stream()
                        .sorted()
                        .collect(Collectors.toCollection(LinkedHashSet::new))))
                .collect(Collectors.toList());

    }


}

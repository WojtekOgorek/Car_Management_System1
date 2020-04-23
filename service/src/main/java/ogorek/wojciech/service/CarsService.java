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
import java.util.function.Function;
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
    //method 6. Map - key is the car model and value is the most valuable car by this model.
    //sorted descending by key

    public Map<String, List<Car>> highestPriceCarModel(){

        return cars
                .stream()
                .collect(Collectors.groupingBy(Car::getModel))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        v -> v.getValue()
                        .stream()
                        .collect(Collectors.groupingBy(Car::getPrice))
                        .entrySet()
                                .stream()
                        .max(Comparator.comparing(Map.Entry::getKey))
                        .orElseThrow()
                        .getValue()
                ));
    }

    //method 7. Method returns car with the highest price. If there are more than one
    //highest priced cars it returns collection of those

    public List<Car> highestPricedCar(){

        Car highestPricedCar = cars
                .stream()
                .max((p1,p2) -> p1.getPrice().compareTo(p2.getPrice()))
                .orElse(null);

        return cars.stream()
                .filter(car -> car.getPrice() == highestPricedCar.getPrice())
                .collect(Collectors.toList());
    }

    //method 8. Method return map where key is car component and value is car collection that
    //contain those components. Map is sorted descending by value

    public Map<String, List<Car>> carWithSameComponents(){

        return cars
                .stream()
                .flatMap(car -> car.getComponents().stream())
                .distinct()
                .collect(Collectors.toMap(Function.identity(), component-> cars
                .stream()
                .filter(car -> car.getComponents().contains(component))
                        .collect(Collectors.toList())));
    }


    //method 9. Method return car collection which price fits in the selected range <a, b>.
    //Collection is sorted alphabetically

    public List<Car> carsPriceRange(int a, int b){
        if(a < 0 || b < 0 || a > b){
            throw new AppException("carsPriceRange arguments are invalid");
        }

        return cars
                .stream()
                .filter(car -> car.getPrice().compareTo(BigDecimal.valueOf(a)) > 0
                && car.getPrice().compareTo(BigDecimal.valueOf(b)) < 0)
        .collect(Collectors.toList());

    }


}

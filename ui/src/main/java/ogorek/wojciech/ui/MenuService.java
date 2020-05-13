package ogorek.wojciech.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import ogorek.wojciech.persistance.data.Statistics;
import ogorek.wojciech.persistance.exception.AppException;
import ogorek.wojciech.persistance.model.Car;
import ogorek.wojciech.persistance.model.enums.Color;
import ogorek.wojciech.persistance.model.enums.SortItem;
import ogorek.wojciech.persistance.repository.CarsConverter;
import ogorek.wojciech.service.CarsService;
import ogorek.wojciech.service.DataGenerator;
import ogorek.wojciech.service.utils.UserDataService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class MenuService {

    private final CarsService carsService;

    public void mainMenu(){
        while(true){
            try{
                int option = chooseOptionFromMainMenu();
                    switch(option) {
                        case 1 -> option1();
                        case 2 -> option2();
                        case 3 -> option3();
                        case 4 -> option4();
                        case 5 -> option5();
                        case 6 -> option6();
                        case 7 -> option7();
                        case 8 -> option8();
                        case 9 -> option9();
                        case 10 -> option10();
                        case 11 -> option11();
                        case 12 ->{
                                UserDataService.close();
                        System.out.println("Have a nice day !");
                        return;
                    }
                        default -> System.out.println("\nWrong option number\n");
                }

            }catch(AppException e){
                System.out.println(e.getMessage());
            }
        }
    }

    private int chooseOptionFromMainMenu(){
        System.out.println("1. Show all cars");
        System.out.println("2. Sort cars");
        System.out.println("3. Get cars with mileage grater than");
        System.out.println("4. Get list of cars grouped by color");
        System.out.println("5. Get price and mileage statistics of cars in list");
        System.out.println("6. Sort cars components");
        System.out.println("7. Get cars grouped by model -> and its highest price");
        System.out.println("8. Get cars with the highest price");
        System.out.println("9. Get cars with same components");
        System.out.println("10. Get cars in the selected price range");
        System.out.println("11. Generate/Load car data");
        System.out.println("12. End of app");
        return UserDataService.getInt("Choose option:");
    }

    private void option1(){ System.out.println(carsService.toString());}

    private void option2() {
        SortItem sortItem = UserDataService.getSortItem();
        boolean descending = UserDataService.getBoolean("Sort descending? Enter y / n");
        List<Car> sortedCars = carsService.sort(sortItem, descending);
        System.out.println(toJson(sortedCars));
    }

    private void option3(){
        double mileage = UserDataService.getDouble("Type mileage");
        List<Car> carsWithGraterMileage = carsService.getAllWithMileageGreaterThan(mileage);
        System.out.println(toJson(carsWithGraterMileage));
    }

    private void option4(){
        Map<Color, Long> countByColor = carsService.countCarsByColors();
        System.out.println(toJson(countByColor));
    }

    private void option5(){
        Statistics statistics = carsService.summarizeMileageAndPrice();
        System.out.println(toJson(statistics));
    }

    private void option6(){
        List<Car> sortingComponents = carsService.sortCarComponents();
        System.out.println(sortingComponents);
    }

    private void option7(){
        Map<String, List<Car>> highestPriceCarModel = carsService.highestPriceCarModel();
        System.out.println(toJson(highestPriceCarModel));
    }
    private void option8(){
        List<Car> highestPricedCar = carsService.highestPricedCar();
        System.out.println(toJson(highestPricedCar));
    }
    private void option9(){
        Map<String, List<Car>> carsWithSameComponents = carsService.carWithSameComponents();
        System.out.println(toJson(carsWithSameComponents));
    }
    private void option10(){
        int a = UserDataService.getInt("Type the low price point: ");
        int b = UserDataService.getInt("Type the high price point: ");
        List<Car> selectedPriceRangeCars = carsService.carsPriceRange(a, b);
        System.out.println(toJson(selectedPriceRangeCars));
    }
    private void option11(){
        DataGenerator dataGenerator = new DataGenerator();
        List<Car> cars = dataGenerator.createACar();

        final String userJsonFile = "userCars.json";
        CarsConverter carsConverter = new CarsConverter(userJsonFile);
        carsConverter.toJson(cars);
        carsConverter.fromJson().ifPresent(System.out::println);

    }


    private static <T> String toJson(T t){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(t);
    }


}

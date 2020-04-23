package ogorek.wojciech.ui;

import lombok.RequiredArgsConstructor;
import ogorek.wojciech.persistance.exception.AppException;
import ogorek.wojciech.service.CarsService;
import ogorek.wojciech.service.utils.UserDataService;

@RequiredArgsConstructor
public class MenuService {

    private final CarsService carsService;

    public void mainMenu(){
        while(true){
            try{
                int option = chooseOptionFromMainMenu();
                    switch(option) {
                        case 1 -> option1();
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




}

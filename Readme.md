# Car Management System 1

Car management system is a simple multi-module java application to show how you can use java streams

## Installation

Use maven -> [link](https://maven.apache.org/download.cgi) <- to install car management system 1.You need to add file paths to json paths to the filename variables.


```bash
#main folder
mvn clean install
#go to ui folder 
cd ui
#go to target folder
cd target
#start app
java -jar ui.jar
```

## Usage

```java

/*
 * ------------- APP OPTIONS IN MENU -------------
 * 
 */

public class MenuService {

    private final CarsService carsService;

    public void mainMenu() {
        while (true) {
            try {
                int option = chooseOptionFromMainMenu();
                switch (option) {
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
                    case 12 -> {
                        UserDataService.close();
                        System.out.println("Have a nice day !");
                        return;
                    }
                    default -> System.out.println("\nWrong option number\n");
                }

            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private int chooseOptionFromMainMenu() {
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
}
```


package ogorek.wojciech.ui;

import ogorek.wojciech.service.CarsService;

public class App {
    public static void main(String[] args) {

        String carsJsonFile = "./resources/data/cars.json";

        CarsService carsService = new CarsService(carsJsonFile);
        MenuService menuService = new MenuService(carsService);
        menuService.mainMenu();

    }
}

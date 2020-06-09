package ogorek.wojciech.service.utils;

import lombok.experimental.UtilityClass;
import ogorek.wojciech.persistance.exception.AppException;
import ogorek.wojciech.persistance.model.enums.Color;
import ogorek.wojciech.persistance.model.enums.SortItem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class UserDataService {

    private Scanner scanner = new Scanner(System.in);


    public int getInt(String message) {
        System.out.println(message);

        String value = scanner.nextLine();
        if (!value.matches("\\d+")) {
            throw new AppException("value must be integer");
        }
        return Integer.parseInt(value);
    }

    public double getDouble(String message){
        System.out.println(message);

        String value = scanner.nextLine();
        if(!value.matches("\\d+\\.\\d+")){
            throw new AppException("value must be double");
        }
        return Double.parseDouble(value);
    }

    public boolean getBoolean(String message){
        System.out.println(message);
        return scanner.nextLine().toLowerCase().equals("y");
    }

    public String getString(String message){
        System.out.println(message);
        return scanner.nextLine();
    }

    public BigDecimal getBigDecimal(String message){
        System.out.println(message);

        String value = scanner.nextLine();
        if(!value.matches("(\\d+\\.)?\\d+")){
            throw new AppException("value must be BigDecimal");
        }
        return new BigDecimal(value);
    }

    public SortItem getSortItem() {
        var counter = new AtomicInteger(1);

        Arrays
                .stream(SortItem.values())
                .forEach(sortItem -> System.out.println(counter.getAndIncrement() + ". " + sortItem));
        int option = getInt("Choose sort item");
        if (option < 1 || option > SortItem.values().length) {
            throw new AppException("incorrect sort item number");
        }

        return SortItem.values()[option - 1];
    }

    public Color getColor(String message){
        System.out.println(message);

        var counter = new AtomicInteger(1);
        Arrays
                .stream(Color.values())
                .forEach(color -> System.out.println(counter.getAndIncrement() + ". " + color));
        var option = UserDataService.getInt("Pick color");
        if(option < 1 || option > Color.values().length){
            throw new AppException("invalid color option");
        }

        return Color.values()[option - 1];
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
    }


}

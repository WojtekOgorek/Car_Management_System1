package ogorek.wojciech.service.utils;

import ogorek.wojciech.persistance.exception.AppException;
import ogorek.wojciech.persistance.model.enums.SortItem;

import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class UsedDataService {

    private Scanner scanner = new Scanner(System.in);


    public int getInt(String message) {
        System.out.println(message);

        String value = scanner.nextLine();
        if (!value.matches("\\d+")) {
            throw new AppException("value must be integer");
        }
        return Integer.parseInt(value);


    }

    public boolean getBoolean(String message){
        System.out.println(message);
        return scanner.nextLine().toLowerCase().equals("y");
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

    public void close() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
    }


}

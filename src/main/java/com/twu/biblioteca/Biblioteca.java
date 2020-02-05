package com.twu.biblioteca;

import com.twu.biblioteca.database.RentalDatabase;
import com.twu.biblioteca.database.UserDatabase;
import com.twu.biblioteca.model.Rental;

import java.util.ArrayList;

public class Biblioteca {

    public enum FILTER {
        AVAILABLE,
        NOT_AVAILABLE,
        ALL
    }

    public enum RESPONSE {
        SUCCESS,
        AUTHORIZATION_ERROR,
        DEFAULT_ERROR
    }

    private BibliotecaUser userManager;

    public Biblioteca() {
        userManager = new BibliotecaUser();
        UserDatabase.getInstance().initialize();
        RentalDatabase.getInstance().initialize();
    }

    public ArrayList<Rental> getItems(FILTER filter) {
        ArrayList<Rental> items = new ArrayList<>();
        switch (filter){
            case AVAILABLE:
                items = RentalDatabase.getInstance().getItems(Rental::isAvailable);
                break;
            case NOT_AVAILABLE:
                items = RentalDatabase.getInstance().getItems(item -> !item.isAvailable());
                break;
            case ALL:
                items = RentalDatabase.getInstance().getItems();
                break;
        }
        return items;
    }

    public RESPONSE doCheckOut(String itemName) {
        if(userManager.getCurrentUser()==null) return RESPONSE.AUTHORIZATION_ERROR;
        Rental item = RentalDatabase.getInstance().getItems().stream()
                .filter(it -> it.getTitle().equals(itemName))
                .findFirst()
                .orElse(null);
        boolean itemExist = item != null;
        if(itemExist) {
            if(!item.isAvailable()) return RESPONSE.DEFAULT_ERROR;
            item.doCheckOut(userManager.getCurrentUser());
            return RESPONSE.SUCCESS;
        }
        return RESPONSE.DEFAULT_ERROR;
    }

    public RESPONSE doReturn(String itemName) {
        if(userManager.getCurrentUser()==null) return RESPONSE.AUTHORIZATION_ERROR;
        Rental item = RentalDatabase.getInstance().getItems().stream()
                .filter(it -> it.getTitle().equals(itemName))
                .findFirst()
                .orElse(null);
        boolean itemExist = item != null;
        if(itemExist) {
            if(item.isAvailable()) return RESPONSE.DEFAULT_ERROR;
            if(userManager.getCurrentUser() != item.getBorrower()) return RESPONSE.AUTHORIZATION_ERROR;
            item.doReturn();
            return RESPONSE.SUCCESS;
        }
        return RESPONSE.DEFAULT_ERROR;
    }

    public BibliotecaUser user() {
        return userManager;
    }
}

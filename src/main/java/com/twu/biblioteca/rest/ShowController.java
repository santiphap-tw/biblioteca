package com.twu.biblioteca.rest;

import com.twu.biblioteca.App;
import com.twu.biblioteca.Biblioteca;
import com.twu.biblioteca.model.Rental;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
public class ShowController {

    @RequestMapping("/show")
    public ArrayList<Rental> showNoParam() {
        return showTwoParams("rental", "available");
    }

    @RequestMapping("/show/{parameter}")
    public ArrayList<Rental> showOneParam(@PathVariable String parameter) {
        switch (parameter){
            case "available":
            case "not_available":
            case "all":
                // Parameter is defining filter
                return showTwoParams("rental", parameter);
            default:
                // Parameter is defining type
                return showTwoParams(parameter, "available");
        }
    }

    @RequestMapping("/show/{type}/{filter}")
    public ArrayList<Rental> showTwoParams(@PathVariable String type, @PathVariable String filter) {
        switch (filter){
            case "available":
                return App.biblioteca.getItems(Biblioteca.FILTER.AVAILABLE).stream()
                        .filter(item -> isSameType(item,type))
                        .collect(Collectors.toCollection(ArrayList::new));
            case "not_available":
                return App.biblioteca.getItems(Biblioteca.FILTER.NOT_AVAILABLE).stream()
                        .filter(item -> isSameType(item,type))
                        .collect(Collectors.toCollection(ArrayList::new));
            case "all":
                return App.biblioteca.getItems(Biblioteca.FILTER.ALL).stream()
                        .filter(item -> isSameType(item,type))
                        .collect(Collectors.toCollection(ArrayList::new));
            default:
                return null;
        }
    }

    private boolean isSameType(Rental item, String type) {
        // If type is Rental (which is superclass), item is considered as a same type
        String rentalCassName = Rental.class.getSimpleName().toLowerCase();
        if(type.equals(rentalCassName)) return true;
        // Check if class name is equal to type
        String className = item.getClass().getSimpleName().toLowerCase();
        return type.equals(className);
    }

}

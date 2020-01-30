package com.twu.biblioteca.cli.operation;

import com.twu.biblioteca.Biblioteca;
import com.twu.biblioteca.cli.BibliotecaApp;
import com.twu.biblioteca.model.AppOperation;
import com.twu.biblioteca.model.Label;
import com.twu.biblioteca.model.User;

import java.util.ArrayList;

public class LogoutOperation extends AppOperation {

    private Biblioteca biblioteca;

    public LogoutOperation(String description, Biblioteca biblioteca) {
        super(description);
        this.biblioteca = biblioteca;
    }

    @Override
    public ArrayList<String> run(String parameter) {
        ArrayList<String> output = new ArrayList<String>();

        User user = biblioteca.user().logout();
        output.add(Label.LOGOUT_SUCCESS.text + user.getName());

        response = BibliotecaApp.RESPONSE.VALID;
        return output;
    }
}

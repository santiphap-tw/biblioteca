package com.twu.biblioteca.cli;

import com.twu.biblioteca.Biblioteca;
import com.twu.biblioteca.cli.operation.*;
import com.twu.biblioteca.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class BibliotecaApp {

    private enum STATE {
        INITIAL,
        RUNNING,
        TERMINATE
    }

    public enum RESPONSE {
        VALID,
        INVALID,
        EXIT
    }

    private STATE state;
    private Map<String, AppOperation> options;
    private AppOperation initialTasks;
    private final AppOperation invalidOption = new InvalidOperation(Label.OPTION_INVALID.text);
    private Scanner sc = new Scanner(System.in);

    public BibliotecaApp() {
        this(new Biblioteca());
    }
    public BibliotecaApp(Biblioteca biblioteca) {
        this.state = STATE.INITIAL;
        // Initialize options
        options = new LinkedHashMap<String, AppOperation>();
        options.put(Label.OPTION_SHOW_ALL_COMMAND.text, new ShowOperation(Label.OPTION_SHOW_ALL.text, biblioteca, Rental.class));
        options.put(Label.OPTION_SHOW_BOOKS_COMMAND.text, new ShowOperation(Label.OPTION_SHOW_BOOKS.text, biblioteca, Book.class));
        options.put(Label.OPTION_SHOW_MOVIES_COMMAND.text, new ShowOperation(Label.OPTION_SHOW_MOVIES.text, biblioteca, Movie.class));
        options.put(Label.OPTION_LOGIN_COMMAND.text, new LoginOperation(Label.OPTION_LOGIN.text, biblioteca));
        options.put(Label.OPTION_LOGOUT_COMMAND.text, new LogoutOperation(Label.OPTION_LOGOUT.text, biblioteca));
        options.put(Label.OPTION_CHECKOUT_COMMAND.text, new CheckOutOperation(Label.OPTION_CHECKOUT.text, biblioteca));
        options.put(Label.OPTION_RETURN_COMMAND.text, new ReturnOperation(Label.OPTION_RETURN.text, biblioteca));
        options.put(Label.OPTION_MY_INFO_COMMAND.text, new MyInfoOperation(Label.OPTION_MY_INFO.text, biblioteca));
        options.put(Label.OPTION_MY_BORROWING_COMMAND.text, new MyBorrowOperation(Label.OPTION_MY_BORROWING.text, biblioteca));
        options.put(Label.OPTION_HELP_COMMAND.text, new StartOperation(Label.OPTION_HELP.text, options));
        options.put(Label.OPTION_EXIT_COMMAND.text, new ExitOperation(Label.OPTION_EXIT.text));
        // Initialize initial tasks
        initialTasks = new StartOperation("", options);
    }

    public void start() {
        while(this.state != BibliotecaApp.STATE.TERMINATE) {
            switch (this.state) {
                case INITIAL:
                    printOutput(initialTasks.run(""));
                    this.state = BibliotecaApp.STATE.RUNNING;
                    break;
                case RUNNING:
                    System.out.print(Label.OPTION_INPUT_PROMPT.text);
                    String option = sc.nextLine();
                    RESPONSE response = selectOption(option);
                    if(response == RESPONSE.EXIT)
                        this.state = BibliotecaApp.STATE.TERMINATE;
                    break;
                case TERMINATE:
                    break;
            }
        }
    }

    public RESPONSE selectOption(String command) {
        // Split command into option & parameter
        String[] commandSplit = command.split(" ", 2);
        String option = commandSplit[0];
        String parameter = commandSplit.length > 1 ? commandSplit[1] : "";

        AppOperation selectedOption = options.getOrDefault(option, invalidOption);
        ArrayList<String> output = selectedOption.run(parameter);
        BibliotecaApp.printOutput(output);
        RESPONSE response = selectedOption.getResponse();
        return response;
    }

    public static void printOutput(ArrayList<String> output){
        output.forEach(System.out::println);
    }
}
package com.twu.biblioteca.unit.cli.operation;

import com.twu.biblioteca.cli.BibliotecaApp;
import com.twu.biblioteca.cli.operation.ExitOperation;
import com.twu.biblioteca.model.Label;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ExitOperationTest {

    private ExitOperation exitOperation;

    @Before
    public void initialize(){
        // Given
        exitOperation = new ExitOperation("");
    }

    @Test
    public void exitTest() {
        // When
        ArrayList<String> output = exitOperation.run("");
        // Then
        boolean isExit = output.stream().anyMatch(text -> text.equals(Label.EXIT.text));
        isExit = isExit & exitOperation.getResponse().equals(BibliotecaApp.RESPONSE.EXIT);
        assertEquals("Exit operation should exit", true, isExit);
    }
}

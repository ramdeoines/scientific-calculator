package com.ramdeo.calculator;

import javafx.application.Application;

public final class Launcher {

    private Launcher() {
        // Entry point only: nobody should create an instance of it.
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
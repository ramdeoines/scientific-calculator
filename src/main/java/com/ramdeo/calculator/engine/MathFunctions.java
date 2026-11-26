package com.ramdeo.calculator.engine;

import com.ramdeo.calculator.exception.CalculatorException;

public final class MathFunctions {

    private MathFunctions() {
        // Utility class: nobody should create an instance of it.
    }

    public static double add(double a, double b) {
        return a + b;
    }

    public static double subtract(double a, double b) {
        return a - b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static double divide(double a, double b) {
        if (b == 0) {
            throw new CalculatorException("Cannot divide by zero");
        }
        return a / b;
    }
}
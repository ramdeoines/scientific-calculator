package com.ramdeo.calculator.engine;

import com.ramdeo.calculator.exception.CalculatorException;

public final class ScientificFunctions {

    // Results smaller than this are treated as exactly zero.
    private static final double ZERO_THRESHOLD = 1e-12;

    private ScientificFunctions() {
        // Utility class: nobody should create an instance of it.
    }

    public static double sin(double value, AngleMode mode) {
        return cleanTinyValue(Math.sin(toRadians(value, mode)));
    }

    public static double cos(double value, AngleMode mode) {
        return cleanTinyValue(Math.cos(toRadians(value, mode)));
    }

    public static double tan(double value, AngleMode mode) {
        double radians = toRadians(value, mode);
        if (cleanTinyValue(Math.cos(radians)) == 0.0) {
            throw new CalculatorException("Tangent is undefined for this angle");
        }
        return cleanTinyValue(Math.tan(radians));
    }

    public static double log10(double value) {
        if (value <= 0) {
            throw new CalculatorException("Logarithm requires a positive number");
        }
        return Math.log10(value);
    }

    public static double ln(double value) {
        if (value <= 0) {
            throw new CalculatorException("Logarithm requires a positive number");
        }
        return Math.log(value);
    }

    public static double sqrt(double value) {
        if (value < 0) {
            throw new CalculatorException("Cannot take the square root of a negative number");
        }
        return Math.sqrt(value);
    }

    public static double power(double base, double exponent) {
        double result = Math.pow(base, exponent);
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new CalculatorException("Result is undefined or too large");
        }
        return result;
    }

    private static double toRadians(double value, AngleMode mode) {
        return mode == AngleMode.DEGREES ? Math.toRadians(value) : value;
    }

    private static double cleanTinyValue(double value) {
        return Math.abs(value) < ZERO_THRESHOLD ? 0.0 : value;
    }
}
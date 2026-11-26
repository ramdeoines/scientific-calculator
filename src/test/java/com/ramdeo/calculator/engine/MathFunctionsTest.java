package com.ramdeo.calculator.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ramdeo.calculator.exception.CalculatorException;
import org.junit.jupiter.api.Test;

class MathFunctionsTest {

    private static final double DELTA = 1e-9;

    @Test
    void addReturnsSum() {
        assertEquals(5.0, MathFunctions.add(2, 3), DELTA);
    }

    @Test
    void subtractReturnsDifference() {
        assertEquals(-1.0, MathFunctions.subtract(2, 3), DELTA);
    }

    @Test
    void multiplyReturnsProduct() {
        assertEquals(6.0, MathFunctions.multiply(2, 3), DELTA);
    }

    @Test
    void divideReturnsQuotient() {
        assertEquals(2.5, MathFunctions.divide(5, 2), DELTA);
    }

    @Test
    void divideByZeroThrowsException() {
        CalculatorException ex = assertThrows(
                CalculatorException.class,
                () -> MathFunctions.divide(5, 0));
        assertEquals("Cannot divide by zero", ex.getMessage());
    }
}
package com.ramdeo.calculator.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ramdeo.calculator.exception.CalculatorException;
import org.junit.jupiter.api.Test;

class ScientificFunctionsTest {

    private static final double DELTA = 1e-9;

    // --- Trigonometry ---

    @Test
    void sinOf30DegreesIsHalf() {
        assertEquals(0.5, ScientificFunctions.sin(30, AngleMode.DEGREES), DELTA);
    }

    @Test
    void sinOf180DegreesIsExactlyZero() {
        assertEquals(0.0, ScientificFunctions.sin(180, AngleMode.DEGREES));
    }

    @Test
    void sinOfHalfPiRadiansIsOne() {
        assertEquals(1.0, ScientificFunctions.sin(Math.PI / 2, AngleMode.RADIANS), DELTA);
    }

    @Test
    void cosOf60DegreesIsHalf() {
        assertEquals(0.5, ScientificFunctions.cos(60, AngleMode.DEGREES), DELTA);
    }

    @Test
    void cosOf90DegreesIsExactlyZero() {
        assertEquals(0.0, ScientificFunctions.cos(90, AngleMode.DEGREES));
    }

    @Test
    void tanOf45DegreesIsOne() {
        assertEquals(1.0, ScientificFunctions.tan(45, AngleMode.DEGREES), DELTA);
    }

    @Test
    void tanOf90DegreesThrowsException() {
        assertThrows(CalculatorException.class,
                () -> ScientificFunctions.tan(90, AngleMode.DEGREES));
    }

    // --- Logarithms ---

    @Test
    void log10OfThousandIsThree() {
        assertEquals(3.0, ScientificFunctions.log10(1000), DELTA);
    }

    @Test
    void log10OfZeroThrowsException() {
        assertThrows(CalculatorException.class, () -> ScientificFunctions.log10(0));
    }

    @Test
    void lnOfEIsOne() {
        assertEquals(1.0, ScientificFunctions.ln(Math.E), DELTA);
    }

    @Test
    void lnOfNegativeThrowsException() {
        assertThrows(CalculatorException.class, () -> ScientificFunctions.ln(-5));
    }

    // --- Square root and power ---

    @Test
    void sqrtOf16IsFour() {
        assertEquals(4.0, ScientificFunctions.sqrt(16), DELTA);
    }

    @Test
    void sqrtOfNegativeThrowsException() {
        assertThrows(CalculatorException.class, () -> ScientificFunctions.sqrt(-1));
    }

    @Test
    void twoToThePowerOfTenIs1024() {
        assertEquals(1024.0, ScientificFunctions.power(2, 10), DELTA);
    }

    @Test
    void zeroToNegativePowerThrowsException() {
        assertThrows(CalculatorException.class, () -> ScientificFunctions.power(0, -1));
    }
}
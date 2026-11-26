package com.ramdeo.calculator.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ramdeo.calculator.exception.CalculatorException;
import org.junit.jupiter.api.Test;

class ExpressionEvaluatorTest {

    private static final double DELTA = 1e-9;

    private static void assertResult(double expected, String expression) {
        assertEquals(expected,
                ExpressionEvaluator.evaluate(expression, AngleMode.DEGREES), DELTA);
    }

    private static void assertFails(String expression) {
        assertThrows(CalculatorException.class,
                () -> ExpressionEvaluator.evaluate(expression, AngleMode.DEGREES));
    }

    // --- Order of operations ---

    @Test
    void multiplicationComesBeforeAddition() {
        assertResult(14, "2+3*4");
    }

    @Test
    void bracketsOverridePrecedence() {
        assertResult(20, "(2+3)*4");
    }

    @Test
    void subtractionRunsLeftToRight() {
        assertResult(3, "10-4-3");
    }

    @Test
    void divisionRunsLeftToRight() {
        assertResult(5, "100/10/2");
    }

    @Test
    void powerRunsRightToLeft() {
        assertResult(512, "2^3^2");
    }

    @Test
    void minusAppliesAfterPower() {
        assertResult(-4, "-2^2");
    }

    @Test
    void negativeExponentWorks() {
        assertResult(0.5, "2^-1");
    }

    @Test
    void minusCanFollowAnOperator() {
        assertResult(8, "5 - -3");
    }

    @Test
    void decimalNumbersWork() {
        assertResult(7, "3.5*2");
    }

    // --- Functions and constants ---

    @Test
    void sinUsesDegrees() {
        assertResult(0.5, "sin(30)");
    }

    @Test
    void sinUsesRadiansWhenAsked() {
        double result = ExpressionEvaluator.evaluate("sin(pi/2)", AngleMode.RADIANS);
        assertEquals(1.0, result, DELTA);
    }

    @Test
    void piConstantWorks() {
        assertResult(2 * Math.PI, "2*pi");
    }

    @Test
    void eConstantWorks() {
        assertResult(1, "ln(e)");
    }

    @Test
    void functionsCanBeCombined() {
        assertResult(6, "sqrt(16)+log(100)");
    }

    @Test
    void functionArgumentCanBeAnExpression() {
        assertResult(1, "sin(30+60)");
    }

    @Test
    void functionsCanBeNested() {
        assertResult(2, "sqrt(sqrt(16))");
    }

    // --- Errors ---

    @Test
    void divisionByZeroFails() {
        assertFails("5/0");
    }

    @Test
    void divisionByZeroInsideBracketsFails() {
        assertFails("5/(3-3)");
    }

    @Test
    void missingClosingBracketFails() {
        assertFails("(2+3");
    }

    @Test
    void missingOperandFails() {
        assertFails("2+");
    }

    @Test
    void twoNumbersInARowFail() {
        assertFails("2 3");
    }

    @Test
    void unknownFunctionFails() {
        assertFails("foo(3)");
    }

    @Test
    void functionWithoutBracketsFails() {
        assertFails("sin 30");
    }

    @Test
    void logOfZeroFails() {
        assertFails("log(0)");
    }

    @Test
    void emptyExpressionFails() {
        assertFails("");
    }

    @Test
    void strayClosingBracketFails() {
        assertFails(")");
    }
}
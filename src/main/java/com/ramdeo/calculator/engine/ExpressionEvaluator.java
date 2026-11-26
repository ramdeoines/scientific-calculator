package com.ramdeo.calculator.engine;

import com.ramdeo.calculator.exception.CalculatorException;
import java.util.List;

public final class ExpressionEvaluator {

    private ExpressionEvaluator() {
        // Utility class: nobody should create an instance of it.
    }

    public static double evaluate(String expression, AngleMode angleMode) {
        List<Token> tokens = new Tokenizer(expression).tokenize();
        double result = new Parser(tokens, angleMode).parse();

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new CalculatorException("Result is undefined or too large");
        }
        return result;
    }
}
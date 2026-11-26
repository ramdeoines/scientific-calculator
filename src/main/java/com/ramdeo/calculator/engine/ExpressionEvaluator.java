package com.ramdeo.calculator.engine;

import java.util.List;

public final class ExpressionEvaluator {

    private ExpressionEvaluator() {
        // Utility class: nobody should create an instance of it.
    }

    public static double evaluate(String expression, AngleMode angleMode) {
        List<Token> tokens = new Tokenizer(expression).tokenize();
        return new Parser(tokens, angleMode).parse();
    }
}
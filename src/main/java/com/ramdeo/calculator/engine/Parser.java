package com.ramdeo.calculator.engine;

import com.ramdeo.calculator.exception.CalculatorException;
import java.util.List;
import java.util.Set;

public final class Parser {

    private static final Set<String> FUNCTIONS =
            Set.of("sin", "cos", "tan", "log", "ln", "sqrt");

    private final List<Token> tokens;
    private final AngleMode angleMode;
    private int position;

    public Parser(List<Token> tokens, AngleMode angleMode) {
        this.tokens = tokens;
        this.angleMode = angleMode;
        this.position = 0;
    }

    public double parse() {
        double result = parseExpression();
        if (current().type() != TokenType.END) {
            throw new CalculatorException("Unexpected '" + current().text() + "' in expression");
        }
        return result;
    }

    // expression = term (("+" | "-") term)*
    private double parseExpression() {
        double result = parseTerm();
        while (current().type() == TokenType.PLUS || current().type() == TokenType.MINUS) {
            TokenType operator = advance().type();
            double right = parseTerm();
            if (operator == TokenType.PLUS) {
                result = MathFunctions.add(result, right);
            } else {
                result = MathFunctions.subtract(result, right);
            }
        }
        return result;
    }

    // term = unary (("*" | "/") unary)*
    private double parseTerm() {
        double result = parseUnary();
        while (current().type() == TokenType.MULTIPLY || current().type() == TokenType.DIVIDE) {
            TokenType operator = advance().type();
            double right = parseUnary();
            if (operator == TokenType.MULTIPLY) {
                result = MathFunctions.multiply(result, right);
            } else {
                result = MathFunctions.divide(result, right);
            }
        }
        return result;
    }

    // unary = ("-" | "+") unary | power
    private double parseUnary() {
        if (current().type() == TokenType.MINUS) {
            advance();
            return -parseUnary();
        }
        if (current().type() == TokenType.PLUS) {
            advance();
            return parseUnary();
        }
        return parsePower();
    }

    // power = primary ("^" unary)?
    private double parsePower() {
        double base = parsePrimary();
        if (current().type() == TokenType.POWER) {
            advance();
            double exponent = parseUnary();
            return ScientificFunctions.power(base, exponent);
        }
        return base;
    }

    // primary = NUMBER | name | name "(" expression ")" | "(" expression ")"
    private double parsePrimary() {
        Token token = advance();
        return switch (token.type()) {
            case NUMBER -> Double.parseDouble(token.text());
            case IDENTIFIER -> parseIdentifier(token.text());
            case LEFT_PAREN -> {
                double value = parseExpression();
                expect(TokenType.RIGHT_PAREN, "Missing closing bracket");
                yield value;
            }
            case END -> throw new CalculatorException("Expression ended unexpectedly");
            default -> throw new CalculatorException("Unexpected '" + token.text() + "'");
        };
    }

    private double parseIdentifier(String name) {
        if (name.equals("pi")) {
            return Math.PI;
        }
        if (name.equals("e")) {
            return Math.E;
        }
        if (!FUNCTIONS.contains(name)) {
            throw new CalculatorException("Unknown name: " + name);
        }

        expect(TokenType.LEFT_PAREN,
                "Function '" + name + "' needs brackets, e.g. " + name + "(30)");
        double argument = parseExpression();
        expect(TokenType.RIGHT_PAREN, "Missing closing bracket");
        return applyFunction(name, argument);
    }

    private double applyFunction(String name, double argument) {
        return switch (name) {
            case "sin" -> ScientificFunctions.sin(argument, angleMode);
            case "cos" -> ScientificFunctions.cos(argument, angleMode);
            case "tan" -> ScientificFunctions.tan(argument, angleMode);
            case "log" -> ScientificFunctions.log10(argument);
            case "ln" -> ScientificFunctions.ln(argument);
            case "sqrt" -> ScientificFunctions.sqrt(argument);
            default -> throw new CalculatorException("Unknown function: " + name);
        };
    }

    // --- Helpers for moving through the token list ---

    private Token current() {
        return tokens.get(position);
    }

    private Token advance() {
        Token token = tokens.get(position);
        if (token.type() != TokenType.END) {
            position++;
        }
        return token;
    }

    private void expect(TokenType type, String errorMessage) {
        if (current().type() != type) {
            throw new CalculatorException(errorMessage);
        }
        advance();
    }
}
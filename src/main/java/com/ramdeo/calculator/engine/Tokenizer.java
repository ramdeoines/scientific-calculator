package com.ramdeo.calculator.engine;

import com.ramdeo.calculator.exception.CalculatorException;
import java.util.ArrayList;
import java.util.List;

public final class Tokenizer {

    private final String input;
    private int position;

    public Tokenizer(String input) {
        if (input == null) {
            throw new CalculatorException("Expression is missing");
        }
        this.input = input;
        this.position = 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (position < input.length()) {
            char current = input.charAt(position);

            if (Character.isWhitespace(current)) {
                position++;
            } else if (Character.isDigit(current) || current == '.') {
                tokens.add(readNumber());
            } else if (Character.isLetter(current)) {
                tokens.add(readIdentifier());
            } else {
                tokens.add(readSymbol(current));
            }
        }

        tokens.add(new Token(TokenType.END, ""));
        return tokens;
    }

    private Token readNumber() {
        int start = position;
        boolean seenDecimalPoint = false;

        while (position < input.length()) {
            char c = input.charAt(position);
            if (c == '.') {
                if (seenDecimalPoint) {
                    throw new CalculatorException("Invalid number format");
                }
                seenDecimalPoint = true;
            } else if (!Character.isDigit(c)) {
                break;
            }
            position++;
        }

        String text = input.substring(start, position);
        if (text.equals(".")) {
            throw new CalculatorException("Invalid number format");
        }
        return new Token(TokenType.NUMBER, text);
    }

    private Token readIdentifier() {
        int start = position;
        while (position < input.length() && Character.isLetter(input.charAt(position))) {
            position++;
        }
        String text = input.substring(start, position).toLowerCase();
        return new Token(TokenType.IDENTIFIER, text);
    }

    private Token readSymbol(char c) {
        TokenType type = switch (c) {
            case '+' -> TokenType.PLUS;
            case '-' -> TokenType.MINUS;
            case '*' -> TokenType.MULTIPLY;
            case '/' -> TokenType.DIVIDE;
            case '^' -> TokenType.POWER;
            case '(' -> TokenType.LEFT_PAREN;
            case ')' -> TokenType.RIGHT_PAREN;
            default -> throw new CalculatorException("Unexpected character: " + c);
        };
        position++;
        return new Token(type, String.valueOf(c));
    }
}
package com.ramdeo.calculator.engine;

public enum TokenType {
    NUMBER,
    IDENTIFIER,   // function names (sin, log...) and constants (pi, e)
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    POWER,
    LEFT_PAREN,
    RIGHT_PAREN,
    END           // marks the end of the input
}
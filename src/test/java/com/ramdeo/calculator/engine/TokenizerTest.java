package com.ramdeo.calculator.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ramdeo.calculator.exception.CalculatorException;
import java.util.List;
import org.junit.jupiter.api.Test;

class TokenizerTest {

    @Test
    void tokenizesSimpleAddition() {
        List<Token> tokens = new Tokenizer("2+3").tokenize();

        assertEquals(List.of(
                new Token(TokenType.NUMBER, "2"),
                new Token(TokenType.PLUS, "+"),
                new Token(TokenType.NUMBER, "3"),
                new Token(TokenType.END, "")), tokens);
    }

    @Test
    void ignoresWhitespace() {
        List<Token> spaced = new Tokenizer("  2   +  3 ").tokenize();
        List<Token> compact = new Tokenizer("2+3").tokenize();

        assertEquals(compact, spaced);
    }

    @Test
    void readsDecimalNumber() {
        List<Token> tokens = new Tokenizer("3.14").tokenize();

        assertEquals(new Token(TokenType.NUMBER, "3.14"), tokens.get(0));
    }

    @Test
    void tokenizesFunctionCall() {
        List<Token> tokens = new Tokenizer("sin(30)").tokenize();

        assertEquals(List.of(
                new Token(TokenType.IDENTIFIER, "sin"),
                new Token(TokenType.LEFT_PAREN, "("),
                new Token(TokenType.NUMBER, "30"),
                new Token(TokenType.RIGHT_PAREN, ")"),
                new Token(TokenType.END, "")), tokens);
    }

    @Test
    void lowercasesIdentifiers() {
        List<Token> tokens = new Tokenizer("SIN").tokenize();

        assertEquals(new Token(TokenType.IDENTIFIER, "sin"), tokens.get(0));
    }

    @Test
    void tokenizesAllSymbols() {
        List<TokenType> types = new Tokenizer("+-*/^()").tokenize().stream()
                .map(Token::type)
                .toList();

        assertEquals(List.of(
                TokenType.PLUS, TokenType.MINUS, TokenType.MULTIPLY,
                TokenType.DIVIDE, TokenType.POWER,
                TokenType.LEFT_PAREN, TokenType.RIGHT_PAREN,
                TokenType.END), types);
    }

    @Test
    void emptyInputGivesOnlyEndToken() {
        List<Token> tokens = new Tokenizer("").tokenize();

        assertEquals(List.of(new Token(TokenType.END, "")), tokens);
    }

    @Test
    void unexpectedCharacterThrowsException() {
        assertThrows(CalculatorException.class,
                () -> new Tokenizer("2 $ 3").tokenize());
    }

    @Test
    void twoDecimalPointsThrowException() {
        assertThrows(CalculatorException.class,
                () -> new Tokenizer("1.2.3").tokenize());
    }

    @Test
    void loneDecimalPointThrowsException() {
        assertThrows(CalculatorException.class,
                () -> new Tokenizer(".").tokenize());
    }

    @Test
    void nullInputThrowsException() {
        assertThrows(CalculatorException.class,
                () -> new Tokenizer(null));
    }
}
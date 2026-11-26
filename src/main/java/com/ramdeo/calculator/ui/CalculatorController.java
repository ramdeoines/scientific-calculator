package com.ramdeo.calculator.ui;

import com.ramdeo.calculator.engine.AngleMode;
import com.ramdeo.calculator.engine.ExpressionEvaluator;
import com.ramdeo.calculator.exception.CalculatorException;
import java.math.BigDecimal;
import java.math.MathContext;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Button;

public class CalculatorController {

    // One button press: what the user sees, and what the engine receives.
    private record Entry(String display, String engine) {
    }

    private final CalculatorView view;
    private final List<Entry> entries = new ArrayList<>();
    private AngleMode angleMode = AngleMode.DEGREES;
    private Entry lastResult;
    private boolean justEvaluated = false;
    private boolean showingError = false;
    private Button angleModeButton;

    public CalculatorController(CalculatorView view) {
        this.view = view;
        for (Button button : view.getButtons()) {
            if (button.getText().equals("DEG")) {
                angleModeButton = button;
            }
            button.setOnAction(event -> handle(button.getText()));
        }
    }

    public void attachKeyboard(Scene scene) {
        // Special keys: Enter, Backspace, Escape, Delete
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            String label = switch (event.getCode()) {
                case ENTER -> "=";
                case BACK_SPACE -> "⌫";
                case ESCAPE, DELETE -> "C";
                default -> null;
            };
            if (label != null) {
                handle(label);
                event.consume();
            }
        });

        // Typed characters: digits, operators, brackets
        scene.addEventHandler(KeyEvent.KEY_TYPED, event -> {
            String label = labelForTypedCharacter(event.getCharacter());
            if (label != null) {
                handle(label);
                event.consume();
            }
        });
    }

    private static String labelForTypedCharacter(String character) {
        return switch (character) {
            case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                 ".", "(", ")", "+", "^", "=", "e" -> character;
            case "-" -> "−";
            case "*" -> "×";
            case "/" -> "÷";
            case "p" -> "π";
            default -> null;
        };
    }

    // --- Reacting to a button press ---

    private void handle(String label) {
        resetErrorIfShowing();

        switch (label) {
            case "C" -> clearAll();
            case "⌫" -> backspace();
            case "=" -> evaluate();
            case "DEG", "RAD" -> toggleAngleMode();
            default -> insert(label);
        }
        view.getExpressionField().setText(displayText());
    }

    private void insert(String label) {
        if (justEvaluated) {
            // After "=", an operator continues from the answer;
            // anything else starts a fresh calculation.
            entries.clear();
            if (isBinaryOperator(label)) {
                entries.add(lastResult);
            }
            justEvaluated = false;
        }
        entries.add(toEntry(label));
    }

    private void clearAll() {
        entries.clear();
        justEvaluated = false;
        view.getResultLabel().setText("0");
    }

    private void backspace() {
        if (!entries.isEmpty()) {
            entries.remove(entries.size() - 1);
        }
        justEvaluated = false;
    }

    private void evaluate() {
        if (entries.isEmpty()) {
            return;
        }
        try {
            double result = ExpressionEvaluator.evaluate(engineText(), angleMode);
            String shown = format(result);
            view.getResultLabel().setText(shown);
            lastResult = new Entry(shown, BigDecimal.valueOf(result).toPlainString());
            justEvaluated = true;
        } catch (CalculatorException e) {
            showError(e.getMessage());
        }
    }

    private void toggleAngleMode() {
        angleMode = (angleMode == AngleMode.DEGREES) ? AngleMode.RADIANS : AngleMode.DEGREES;
        angleModeButton.setText(angleMode == AngleMode.DEGREES ? "DEG" : "RAD");
    }

    // --- Errors ---

    private void showError(String message) {
        view.getResultLabel().setText(message);
        view.getResultLabel().getStyleClass().add("error");
        showingError = true;
        justEvaluated = false;
    }

    private void resetErrorIfShowing() {
        if (showingError) {
            view.getResultLabel().getStyleClass().remove("error");
            view.getResultLabel().setText("0");
            showingError = false;
        }
    }

    // --- Turning button labels into text ---

    private static Entry toEntry(String label) {
        return switch (label) {
            case "−" -> new Entry("−", "-");
            case "×" -> new Entry("×", "*");
            case "÷" -> new Entry("÷", "/");
            case "√" -> new Entry("√(", "sqrt(");
            case "π" -> new Entry("π", "pi ");
            case "e" -> new Entry("e", "e ");
            case "sin", "cos", "tan", "log", "ln" -> new Entry(label + "(", label + "(");
            default -> new Entry(label, label);
        };
    }

    private static boolean isBinaryOperator(String label) {
        return label.equals("+") || label.equals("−") || label.equals("×")
                || label.equals("÷") || label.equals("^");
    }

    private String displayText() {
        return entries.stream().map(Entry::display).collect(Collectors.joining());
    }

    private String engineText() {
        return entries.stream().map(Entry::engine).collect(Collectors.joining());
    }

    // --- Showing numbers nicely ---

    private static String format(double value) {
        if (value == Math.rint(value) && Math.abs(value) < 1e15) {
            return String.valueOf((long) value);
        }
        return new BigDecimal(value)
                .round(new MathContext(12))
                .stripTrailingZeros()
                .toString();
    }
}   
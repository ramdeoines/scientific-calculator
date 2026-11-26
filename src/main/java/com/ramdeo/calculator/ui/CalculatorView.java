package com.ramdeo.calculator.ui;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class CalculatorView {

    private static final String[][] BUTTON_LABELS = {
        {"DEG", "(",  ")",  "C",  "⌫"},
        {"sin", "cos", "tan", "log", "ln"},
        {"√",   "7",  "8",  "9",  "÷"},
        {"^",   "4",  "5",  "6",  "×"},
        {"π",   "1",  "2",  "3",  "−"},
        {"e",   "0",  ".",  "=",  "+"}
    };

    private final VBox root = new VBox(12);
    private final TextField expressionField = new TextField();
    private final Label resultLabel = new Label("0");
    private final List<Button> buttons = new ArrayList<>();

    public CalculatorView() {
        root.getStyleClass().add("root-pane");

        GridPane buttonGrid = createButtonGrid();
        VBox.setVgrow(buttonGrid, Priority.ALWAYS);

        root.getChildren().addAll(createDisplay(), buttonGrid);
    }

    private VBox createDisplay() {
        expressionField.getStyleClass().add("expression");
        expressionField.setEditable(false);
        expressionField.setFocusTraversable(false);

        resultLabel.getStyleClass().add("result");
        resultLabel.setMaxWidth(Double.MAX_VALUE);
        resultLabel.setAlignment(Pos.CENTER_RIGHT);

        return new VBox(4, expressionField, resultLabel);
    }

    private GridPane createButtonGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);

        int rows = BUTTON_LABELS.length;
        int columns = BUTTON_LABELS[0].length;

        for (int c = 0; c < columns; c++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / columns);
            grid.getColumnConstraints().add(column);
        }
        for (int r = 0; r < rows; r++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / rows);
            grid.getRowConstraints().add(row);
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                Button button = createButton(BUTTON_LABELS[r][c]);
                grid.add(button, c, r);
                buttons.add(button);
            }
        }
        return grid;
    }

    private Button createButton(String label) {
        Button button = new Button(label);
        button.getStyleClass().addAll("calc-button", styleClassFor(label));
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setFocusTraversable(false);
        return button;
    }

    private static String styleClassFor(String label) {
        return switch (label) {
            case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "." -> "digit";
            case "+", "−", "×", "÷", "^" -> "operator";
            case "=" -> "equals";
            case "C", "⌫" -> "clear";
            default -> "function";
        };
    }

    public VBox getRoot() {
        return root;
    }

    public TextField getExpressionField() {
        return expressionField;
    }

    public Label getResultLabel() {
        return resultLabel;
    }

    public List<Button> getButtons() {
        return buttons;
    }
}
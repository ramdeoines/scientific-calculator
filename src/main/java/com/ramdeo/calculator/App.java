package com.ramdeo.calculator;

import com.ramdeo.calculator.ui.CalculatorView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        CalculatorView view = new CalculatorView();
        Scene scene = new Scene(view.getRoot(), 400, 620);
        scene.getStylesheets().add(
                getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle("Scientific Calculator");
        stage.setMinWidth(340);
        stage.setMinHeight(520);
        stage.setScene(scene);
        stage.show();
    }
}
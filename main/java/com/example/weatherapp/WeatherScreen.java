package com.example.weatherapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




public class WeatherScreen {

    private Scene scene;


    public WeatherScreen(Stage primaryStage) {
        primaryStage.setTitle("Weather Information");

        // Build the main page stuff here
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> goBackScreen(primaryStage));

        VBox layout = new VBox(10, backButton);
        layout.setPadding(new Insets(20));

        this.scene = new Scene(layout, 1920, 1080);
        scene.getStylesheets().add(getClass().getResource("/static/styles.css").toExternalForm());
    }

    public Scene getScene() {
        return this.scene;
    }

    private void goBackScreen(Stage primaryStage) {
        primaryStage.setScene(WeatherApp.getMainScene());
    }
    
}

package com.example.weatherapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;




public class WeatherApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.getChildren().add(new Text("Hello, world!"));

        Scene scene = new Scene(root, 1920, 1080);
        scene.getStylesheets().add(getClass().getResource("/static/styles.css").toExternalForm());

        primaryStage.setTitle("Weather App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

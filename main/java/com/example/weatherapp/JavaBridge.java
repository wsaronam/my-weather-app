package com.example.weatherapp;

import javafx.application.Platform;
import javafx.scene.control.TextField;




public class JavaBridge {
    private TextField cityInput;

    public JavaBridge(TextField cityInput) {
        this.cityInput = cityInput;
    }

    // this will update the JavaFX UI with the city name
    public void setCity(String city) {
        System.out.println("found city: " + city); // delete later
        Platform.runLater(() -> cityInput.setText(city));
    }
}

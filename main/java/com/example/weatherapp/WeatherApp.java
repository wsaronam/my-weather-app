package com.example.weatherapp;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

import javafx.stage.Stage;

import org.json.JSONObject;






public class WeatherApp extends Application {

    private static Scene mainScene;
    private TextField cityInput;




    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("My Weather App");

        // Build the main page stuff here
        cityInput = new TextField();
        cityInput.setPromptText("Enter city name...");
        Button searchButton = new Button("Get Weather");
        Button locateButton = new Button("Locate Me");
        searchButton.setOnAction(e -> getWeather(primaryStage));
        locateButton.setOnAction(e -> getLocation());

        VBox layout = new VBox(10, cityInput, searchButton, locateButton);
        layout.setPadding(new Insets(20));

        this.mainScene = new Scene(layout, 1920, 1080);
        mainScene.getStylesheets().add(getClass().getResource("/static/styles.css").toExternalForm());
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public static Scene getMainScene() { // Getter for the main scene
        return mainScene;
    }


    private void getLocation() {
        String url = "https://ipapi.co/json/"; // api to get location

        // use separate thread for network request
        new Thread(() -> {
            try {
                // create HTTP client
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

                // send request and get response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // parse data (JSON object)
                JSONObject json = new JSONObject(response.body());
                String city = json.getString("city");

                // update UI with found location
                Platform.runLater(() -> {
                    cityInput.setText(city); // Update text field
                });

            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void getWeather(Stage primaryStage) {
        String cityName = cityInput.getText();
        WeatherScreen weatherScreen = new WeatherScreen(primaryStage, cityName);
        primaryStage.setScene(weatherScreen.getScene());
    }




    public static void main(String[] args) {
        launch(args);
    }
}

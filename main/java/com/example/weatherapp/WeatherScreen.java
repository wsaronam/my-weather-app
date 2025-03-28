package com.example.weatherapp;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;




public class WeatherScreen {

    private Scene scene;

    private String cityName;
    private String temperature;
    private String condition;
    private String humidity;
    private String windSpeed;
    private String pressure;



    public WeatherScreen(Stage primaryStage, String cityName) {
        this.cityName = cityName;


        getWeatherData(cityName);


        primaryStage.setTitle("Weather Information");

        // Build the main page stuff here
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> goBackScreen(primaryStage));

        Label cityNameLabel = new Label("Weather data for: " + this.cityName);

        VBox layout = new VBox(10, backButton, cityNameLabel);
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


    private JSONObject getWeatherData(String cityName) {
        if (!cityName.isEmpty()) {
            JSONObject json = new JSONObject();
            String apiUrl = "http://localhost:8080/api/weather/" + cityName;


            new Thread(() -> {
                try {
                    // create HTTP client
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .build();

                    // send request and get response
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    // parse data (JSON object)
                    json = new JSONObject(response.body());

                } 
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            return json;
        }
    }
    
}

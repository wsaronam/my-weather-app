package com.example.weatherapp;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicReference;

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

        // insert weather data into html
        // document.getElementById("city-name").textContent = `${currentWeather.name}, ${currentWeather.sys.country}`;
        // document.getElementById("temperature").textContent = `${currentWeather.main.temp}F`;
        // // document.getElementById("feels-like").textContent = `${weatherData.main.feels_like}F`;
        // document.getElementById("condition").textContent = `${currentWeather.weather[0].main} - ${currentWeather.weather[0].description}`;
        // document.getElementById("humidity").textContent = `${currentWeather.main.humidity}%`;
        // document.getElementById("wind-speed").textContent = `${currentWeather.wind.speed} m/s`;
        // document.getElementById("pressure").textContent = `${currentWeather.main.pressure} hPa`;

        JSONObject weatherData = getWeatherData(cityName);
        JSONObject currentWeather = weatherData.getJSONObject("currentWeather");
        this.temperature = String.valueOf(currentWeather.getJSONObject("main").getInt("temp"));



        primaryStage.setTitle("Weather Information");

        // Build the main page stuff here
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> goBackScreen(primaryStage));

        Label cityNameLabel = new Label("Weather data for: " + this.cityName);
        Label cityTempLabel = new Label("Temperature: " + this.temperature);

        VBox layout = new VBox(10, backButton, cityNameLabel, cityTempLabel);
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
        AtomicReference<JSONObject> jsonRef = new AtomicReference<>(new JSONObject());  // atomicreference is used to reference new data in thread

        if (!cityName.isEmpty()) {
            String apiUrl = "http://localhost:8080/api/weather/" + cityName;

            Thread thread = new Thread(() -> {
                try {
                    // create HTTP client
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .build();

                    // send request and get response
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    // parse data (JSON object)
                    JSONObject json = new JSONObject(response.body());
                    jsonRef.set(json);
                } 
                catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread.start();

            try {
                thread.join(); // wait for the thread to complete first
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        return jsonRef.get();
    }
    
}

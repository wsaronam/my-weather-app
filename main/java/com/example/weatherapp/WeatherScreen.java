package com.example.weatherapp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONObject;
import org.json.JSONArray;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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

        // insert weather data variables into labels
        JSONObject weatherData = getWeatherData(cityName);
        JSONObject currentWeather = weatherData.getJSONObject("currentWeather");
        getFiveDayForecast(weatherData);
        this.cityName = cityName;
        this.temperature = String.valueOf(currentWeather.getJSONObject("main").getInt("temp")) + " F";
        this.condition = currentWeather.getJSONArray("weather").getJSONObject(0).getString("main") + " - " + 
            currentWeather.getJSONArray("weather").getJSONObject(0).getString("description");
        this.humidity = String.valueOf(currentWeather.getJSONObject("main").getInt("humidity")) + "%";
        this.windSpeed = String.valueOf(currentWeather.getJSONObject("wind").getInt("speed")) + " m/s";
        this.pressure = String.valueOf(currentWeather.getJSONObject("main").getInt("pressure")) + " hPa";



        primaryStage.setTitle("Weather Information");

        // Build the main page stuff here
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> goBackScreen(primaryStage));

        Label cityNameLabel = new Label("Weather data for: " + this.cityName);
        Label cityTempLabel = new Label("Temperature: " + this.temperature);
        Label cityCondLabel = new Label("Condition: " + this.condition);
        Label cityHumidLabel = new Label("Humidity: " + this.humidity);
        Label cityWindLabel = new Label("Wind Speed: " + this.windSpeed);
        Label cityPressLabel = new Label("Pressure: " + this.pressure);

        VBox layout = new VBox(10, backButton, cityNameLabel, cityTempLabel, cityCondLabel, cityHumidLabel, cityWindLabel, cityPressLabel);
        layout.setPadding(new Insets(20));

        this.scene = new Scene(layout, 1920, 1080);
        setWeatherBackground(layout, this.condition); 
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


    private static void setWeatherBackground(VBox root, String condition) {
        String imageUrl;

        switch (condition) {
            case "Clear":
                imageUrl = "https://plus.unsplash.com/premium_photo-1727730047398-49766e915c1d?q=80&w=2712&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
                break;
            case "Clouds":
                imageUrl = "https://plus.unsplash.com/premium_photo-1667689956673-8737a299aa8c?q=80&w=2576&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
                break;
            case "Rain":
                imageUrl = "https://images.unsplash.com/photo-1607500098489-1991d1fbab7a?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
                break;
            case "Snow":
                imageUrl = "https://plus.unsplash.com/premium_photo-1669325007869-d3b17d2d31e6?q=80&w=2575&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
                break;
            case "Thunderstorm":
                imageUrl = "https://plus.unsplash.com/premium_photo-1673278171340-99b4dbf0418f?q=80&w=2572&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
                break;
            default:
                imageUrl = "https://plus.unsplash.com/premium_photo-1701646600064-3365efea1ba8?q=80&w=2664&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";
        }

        // create the image for background
        Image backgroundImage = new Image(imageUrl, true); // second argument for background loading

        // edit background image properties
        BackgroundImage bgImage = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT, // horizontal
            BackgroundRepeat.NO_REPEAT, // vertical
            BackgroundPosition.CENTER,
            
            // backgroundsize arguments:
            // width, height, widthAsPercentage, heightAsPercentage, contain, cover
            new BackgroundSize(100, 100, true, true, true, true)
        );

        // wait for the image to load
        backgroundImage.progressProperty().addListener((obs, oldProgress, newProgress) -> {
            if (newProgress.doubleValue() == 1.0) { 
                // set background to VBox
                root.setBackground(new Background(bgImage));
            }
        });
    }
    

    private static Map<String, JSONObject> getFiveDayForecast(JSONObject weatherData) {
        JSONObject fiveDayForecast = weatherData.getJSONObject("fiveDayForecast");
        Object[] forecastArr = fiveDayForecast.toMap().values().toArray();

        Map<String, JSONObject> dailyForecasts = new LinkedHashMap<>(); // store the desired information here

        for (Object entry : forecastArr) {
            System.out.println(entry);
            // String dateTime = entry.getString("dt_txt");
            // String date = dateTime.split(" ")[0]; // extract dates in this format (YYYY-MM-DD)

            // // get the mid-day forecast
            // if (!dailyForecasts.containsKey(date) && dateTime.contains("12:00:00")) {
            //     dailyForecasts.put(date, entry);
            // }
        }

        return dailyForecasts;
    }
}

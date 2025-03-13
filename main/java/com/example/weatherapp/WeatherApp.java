package com.example.weatherapp;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

import javafx.stage.Stage;
import netscape.javascript.JSObject;




public class WeatherApp extends Application {

    private TextField cityInput;

    // create webview so we can run JS for geolocation
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("My Weather App");

        // Build the main page stuff here
        cityInput = new TextField();
        cityInput.setPromptText("Enter city name...");
        Button searchButton = new Button("Get Weather");
        Button locateButton = new Button("Locate Me");
        searchButton.setOnAction(e -> getWeather());
        locateButton.setOnAction(e -> getLocation(cityInput));


        VBox layout = new VBox(10, cityInput, searchButton, locateButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 1920, 1080);
        scene.getStylesheets().add(getClass().getResource("/static/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void getLocation(TextField cityInput) {

        webEngine.setJavaScriptEnabled(true);

        // use the Java object in JS
        JSObject window = (JSObject) webEngine.executeScript("window");
        JavaBridge bridge = new JavaBridge(cityInput);
        window.setMember("javaApp", bridge);
        
        // build JS function for geolocation
        String script = """
            function(position) {
                console.log("Location permission granted");
                let lat = position.coords.latitude;
                let lon = position.coords.longitude;
                console.log("Latitude: " + lat + ", Longitude: " + lon);
            },
            function(error) {
                console.error("Geolocation error:", error);
                alert("Geolocation error: " + error.message);
            }
                
            // navigator.geolocation.getCurrentPosition(
            //     function(position) {
            //         let lat = position.coords.latitude;
            //         let lon = position.coords.longitude;

            //         // Send the lat and lon to the backend to get weather info
            //         fetch(`http://localhost:8080/api/weather/location?lat=${lat}&lon=${lon}`)
            //             .then(response => response.text())
            //             .then(data => {
            //                 console.log("Received city from backend: " + data); // delete later 
            //                 window.javaApp.setCity(data);
            //             })
            //             .catch(error => console.error('Error fetching location:', error));
            //     }, 
            //     function(error) {
            //         console.error("Error getting location:", error);
            //     }
            // );
        """;

        // temporary check for errors  DELETE LATER
        webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldException, newException) -> {
            if (newException != null) {
                newException.printStackTrace();
            }
        });

        // run the JS in WebView
        webEngine.loadContent("<html><script>" + script + "</script></html>");
    }


    private void getWeather() {
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}

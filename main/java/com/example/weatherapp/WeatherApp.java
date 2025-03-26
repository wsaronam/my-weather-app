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

//import javafx.scene.web.WebView;
//import javafx.scene.web.WebEngine;

import javafx.stage.Stage;
//import netscape.javascript.JSObject;

import org.json.JSONObject;






public class WeatherApp extends Application {

    private TextField cityInput;

    // create webview so we can run JS for geolocation
    // WebView webView = new WebView();
    // WebEngine webEngine = webView.getEngine();




    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("My Weather App");
        //webEngine.setJavaScriptEnabled(true);

        // delete his later.  jsut for debugging for now
        // webEngine.setOnAlert(event -> 
        //     System.out.println("webview alert: " + event.getData())
        // );

        // wait for the page to be loaded and then set javaApp
        // webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
        //     if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
        //         JSObject window = (JSObject) webEngine.executeScript("window");
        //         JavaBridge bridge = new JavaBridge(cityInput);
        //         window.setMember("javaApp", bridge);
        //     }
        // });


        // Build the main page stuff here
        cityInput = new TextField();
        cityInput.setPromptText("Enter city name...");
        Button searchButton = new Button("Get Weather");
        Button locateButton = new Button("Locate Me");
        searchButton.setOnAction(e -> getWeather());
        locateButton.setOnAction(e -> getLocation());

        VBox layout = new VBox(10, cityInput, searchButton, locateButton);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 1920, 1080);
        scene.getStylesheets().add(getClass().getResource("/static/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        // // set up javaApp stuff
        // JSObject window = (JSObject) webEngine.executeScript("window");
        // JavaBridge bridge = new JavaBridge(cityInput);

        // // connect javaApp and JavaBridge
        // window.setMember("javaApp", bridge);
    }


    // private void getLocation(TextField cityInput) {

    //     // temporary check for errors  DELETE LATER
    //     webEngine.getLoadWorker().exceptionProperty().addListener((obs, oldException, newException) -> {
    //         if (newException != null) {
    //             System.out.println("JS error: ");
    //             newException.printStackTrace();
    //         }
    //     });

        
    //     // // TEST SCRIPT
    //     // String script = """
    //     //     alert("testing JavaBridge?");

    //     //     if (window.javaApp) {
    //     //         alert("javabridge exists, calling setCity()");
    //     //         window.javaApp.setCity("Testttt");
    //     //     } 
    //     //     else {
    //     //         alert("JavaBridge undefined");
    //     //     }
    //     // """;


    //     // new test JS script
    //     String script = """
    //         var xhr = new XMLHttpRequest();
    //         xhr.open("GET", "https://ipapi.co/json/", true);
    //         xhr.onreadystatechange = function () {
    //             if (xhr.readyState == 4 && xhr.status == 200) {
    //                 alert("raw response: " + xhr.responseText);
    //                 var data = JSON.parse(xhr.responseText);
    //                 alert("Full API Response: " + JSON.stringify(data));
    //                 let city = data.city;
    //                 alert("city: " + city);

    //                 if (window.javaApp) {
    //                     alert("Calling setCity() with: " + city);
    //                     window.javaApp.setCity(city);
    //                 } 
    //                 else {
    //                     alert("javaApp undefined or JavaBridge not connected");
    //                 }
    //             }
    //         };
    //         xhr.onerror = function () {
    //             alert("xhr request failed");
    //             console.log("xhr request failed");
    //         };
    //         xhr.send();
    //     """;
        
    //     // // new test JS script
    //     // String script = """
    //     //     alert("WebView script is running!");
    //     //     fetch("https://ipapi.co/json/")
    //     //         .then(response => response.json())
    //     //         .then(data => {
    //     //             let city = data.city;
    //     //             console.log("IP-based location:", city);
    //     //             alert("IP-based location:", city);

    //     //             if (window.javaApp) {
    //     //                 console.log("calling setCity()");
    //     //                 alert("calling setCity()");
    //     //                 window.javaApp.setCity(city);
    //     //             }
    //     //             else {
    //     //                 console.log("javaApp undefined or JavaBridge not connected");
    //     //                 alert("javaApp undefined or JavaBridge not connected");
    //     //             }
                    
    //     //         })
    //     //         .catch(error => console.error("Error getting IP-based location:", error));
    //     // """;
        
    //     // run the JS in WebView
    //     //webEngine.loadContent("<html><script>" + script + "</script></html>");
    //     //webEngine.executeScript(script);

    //     // run script only if webengine or javaapp/javabridge connected
    //     webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
    //         if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
    //             webEngine.loadContent("<html><script>" + script + "</script></html>");
    //         }
    //     });
    // }

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


    private void getWeather() {

    }


    public static void main(String[] args) {
        launch(args);
    }
}

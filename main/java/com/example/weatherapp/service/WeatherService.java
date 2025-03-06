package com.example.weatherapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.weatherapp.GeolocationResponse;
import org.springframework.web.client.RestTemplate;




@Service
public class WeatherService {
    private final WebClient webClient;
    private final RestTemplate restTemplate;
    String api_key = "b8f6201d2e96ec7d1d437a6aa22b37c9";
    String unitType = "imperial";  // change this to metric for Celsius. Omit it for Kelvin

    public WeatherService(WebClient.Builder webClientBuilder, RestTemplate restTemplate) {
        this.webClient = webClientBuilder.baseUrl("https://api.openweathermap.org/data/2.5").build();
        this.restTemplate = restTemplate;
    }

    public String getCityFromCoordinates(double lat, double lon) {
        String url = String.format("https://api.openweathermap.org/geo/1.0/reverse?lat=%f&lon=%f&limit=1&appid=%s", lat, lon, api_key);

        // response as an array.  api will return array of locations even if its just one element
        GeolocationResponse[] response = restTemplate.getForObject(url, GeolocationResponse[].class);
        if (response != null && response.length > 0) {
            return response[0].getName(); // get name of city
        } 
        else {
            return "Unknown Location";
        }
    }

    public String getWeather(String city) {
        return this.webClient.get()
            .uri("/weather?q=" + city + "&units=" + unitType + "&appid=" + api_key)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public String getFiveDayForecast(String city) {
        return this.webClient.get()
            .uri("/forecast?q=" + city + "&units=" + unitType + "&appid=" + api_key)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}

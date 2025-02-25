package com.example.weatherapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;




@Service
public class WeatherService {
    private final WebClient webClient;
    String api_key = "b8f6201d2e96ec7d1d437a6aa22b37c9";

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openweathermap.org/data/2.5").build();
    }

    public String getWeather(String city) {
        return this.webClient.get()
            .uri("/weather?q=" + city + "&appid=" + api_key)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}

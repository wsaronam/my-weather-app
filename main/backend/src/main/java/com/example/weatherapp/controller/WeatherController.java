package com.example.weatherapp.controller;

import com.example.weatherapp.service.WeatherService;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.*;





@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;
    private final ObjectMapper objectMapper;
    

    public WeatherController(WeatherService weatherService, ObjectMapper objectMapper) {
        this.weatherService = weatherService;
        this.objectMapper = objectMapper;
    }


    @GetMapping("/location")
    public String getLocation(@RequestParam double lat, @RequestParam double lon) {
        // get city using the lat/lon coordinates
        return weatherService.getCityFromCoordinates(lat, lon);
    }


    @GetMapping("/{city}")
    public Map<String, Object> getWeather(@PathVariable String city) {
        Map<String, Object> response = new HashMap<>();

        try {
            // get current weather
            String currentWeatherJson = weatherService.getWeather(city);
            JsonNode currentWeather = objectMapper.readTree(currentWeatherJson); // converts to JSON object
            response.put("currentWeather", currentWeather);

            // get 5-day forecast
            String fiveDayForecastJson = weatherService.getFiveDayForecast(city);
            JsonNode fiveDayForecast = objectMapper.readTree(fiveDayForecastJson); // converts to JSON object
            response.put("fiveDayForecast", fiveDayForecast);
        }
        catch (Exception e) {
            response.put("error", "Failed to process weather data: " + e.getMessage());
        }
        
        return response; // return both in a single JSON object
    }
}

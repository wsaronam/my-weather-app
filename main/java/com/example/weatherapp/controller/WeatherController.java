package com.example.weatherapp.controller;

import com.example.weatherapp.service.WeatherService;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{city}")
    public String getWeather(@PathVariable String city) {
        return weatherService.getWeather(city);
    }
}

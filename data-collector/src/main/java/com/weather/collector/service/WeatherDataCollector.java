package com.weather.collector.service;

import com.weather.common.model.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class WeatherDataCollector {
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Random random;
    private final List<String> locations;
    
    public WeatherDataCollector() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
        this.random = new Random();
        this.locations = Arrays.asList("New York", "London", "Tokyo", "Sydney", "Paris");
    }
    
    public void collectAndStore() {
        for (String location : locations) {
            try {
                WeatherData weatherData = collectWeatherData(location);
                storeWeatherData(weatherData);
            } catch (Exception e) {
                System.err.println("Error collecting weather data for " + location + ": " + e.getMessage());
            }
        }
    }
    
    private WeatherData collectWeatherData(String location) throws Exception {
        // Simulate API call with mock data
        double temperature = 15.0 + (random.nextDouble() * 20.0);
        double humidity = 40.0 + (random.nextDouble() * 40.0);
        double pressure = 1000.0 + (random.nextDouble() * 50.0);
        
        return new WeatherData(location, temperature, humidity, pressure, "external-api");
    }
    
    private void storeWeatherData(WeatherData weatherData) throws Exception {
        // Simulate HTTP POST to weather-api
        String weatherApiUrl = System.getenv("WEATHER_API_URL");
        if (weatherApiUrl == null) {
            weatherApiUrl = "http://localhost:8080";
        }
        
        String json = objectMapper.writeValueAsString(weatherData);
        System.out.println("Collected weather data for " + weatherData.getLocation() + ": " + json);
        
        // In a real implementation, this would POST to the weather-api
        // For now, just log the data
    }
    
    public void shutdown() {
        try {
            httpClient.close();
        } catch (Exception e) {
            System.err.println("Error closing HTTP client: " + e.getMessage());
        }
    }
}
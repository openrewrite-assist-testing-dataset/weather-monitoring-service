package com.weather.api.resources;

import com.weather.api.db.WeatherDAO;
import com.weather.common.model.WeatherData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherResourceTest {

    @Mock
    private WeatherDAO weatherDAO;

    private WeatherResource weatherResource;

    @BeforeEach
    void setUp() {
        weatherResource = new WeatherResource(weatherDAO);
    }

    @Test
    void getCurrentWeather_ReturnsWeatherData_WhenDataExists() {
        // Given
        String location = "London";
        WeatherData weatherData = new WeatherData(location, 15.5, 65.0, 1013.25, "test");
        when(weatherDAO.getCurrentWeather(location)).thenReturn(Optional.of(weatherData));

        // When
        Response response = weatherResource.getCurrentWeather("testUser", location, null);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(weatherData, response.getEntity());
    }

    @Test
    void getCurrentWeather_ReturnsNotFound_WhenDataDoesNotExist() {
        // Given
        String location = "London";
        when(weatherDAO.getCurrentWeather(location)).thenReturn(Optional.empty());

        // When
        Response response = weatherResource.getCurrentWeather("testUser", location, null);

        // Then
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void getHistoricalWeather_ReturnsHistoricalData() {
        // Given
        String location = "London";
        List<WeatherData> historicalData = Arrays.asList(
            new WeatherData(location, 15.5, 65.0, 1013.25, "test"),
            new WeatherData(location, 16.0, 60.0, 1012.0, "test")
        );
        when(weatherDAO.getHistoricalWeather(eq(location), any(Date.class), any(Date.class)))
            .thenReturn(historicalData);

        // When
        Response response = weatherResource.getHistoricalWeather("testUser", location, null, null, null);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(historicalData, response.getEntity());
    }

    @Test
    void submitWeatherData_ReturnsCreated_WhenDataSubmittedSuccessfully() {
        // Given
        WeatherData weatherData = new WeatherData("London", 15.5, 65.0, 1013.25, "test");

        // When
        Response response = weatherResource.submitWeatherData("testUser", weatherData, null);

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    void getAllLocations_ReturnsLocationList() {
        // Given
        List<String> locations = Arrays.asList("London", "Paris", "Tokyo");
        when(weatherDAO.getAllLocations()).thenReturn(locations);

        // When
        Response response = weatherResource.getAllLocations("testUser", null);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(locations, response.getEntity());
    }
}
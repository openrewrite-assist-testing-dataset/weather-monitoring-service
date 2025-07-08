package com.weather.api.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class WeatherApiConfiguration extends Configuration {
    
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();
    
    @JsonProperty("jwtSecret")
    private String jwtSecret = "weather-secret-key-2021";
    
    @JsonProperty("apiKeys")
    private List<String> apiKeys = List.of("weather-api-key-001", "weather-api-key-002");
    
    @JsonProperty("weatherApiUrl")
    private String weatherApiUrl = "https://api.weather.com/v1";
    
    @JsonProperty("dataRetentionDays")
    private int dataRetentionDays = 30;

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public List<String> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(List<String> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public String getWeatherApiUrl() {
        return weatherApiUrl;
    }

    public void setWeatherApiUrl(String weatherApiUrl) {
        this.weatherApiUrl = weatherApiUrl;
    }

    public int getDataRetentionDays() {
        return dataRetentionDays;
    }

    public void setDataRetentionDays(int dataRetentionDays) {
        this.dataRetentionDays = dataRetentionDays;
    }
}
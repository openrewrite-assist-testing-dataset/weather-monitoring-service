package com.weather.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class WeatherData {
    @JsonProperty
    @NotNull
    private String location;
    
    @JsonProperty
    private double temperature;
    
    @JsonProperty
    private double humidity;
    
    @JsonProperty
    private double pressure;
    
    @JsonProperty
    private Date timestamp;
    
    @JsonProperty
    private String source;

    public WeatherData() {
        this.timestamp = new Date();
    }

    public WeatherData(String location, double temperature, double humidity, double pressure, String source) {
        this.location = location;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.source = source;
        this.timestamp = new Date();
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    
    public double getHumidity() { return humidity; }
    public void setHumidity(double humidity) { this.humidity = humidity; }
    
    public double getPressure() { return pressure; }
    public void setPressure(double pressure) { this.pressure = pressure; }
    
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
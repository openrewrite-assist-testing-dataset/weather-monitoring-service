package com.weather.api.db;

import com.weather.common.model.WeatherData;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(WeatherData.class)
public interface WeatherDAO {
    
    @SqlUpdate("INSERT INTO weather_data (location, temperature, humidity, pressure, timestamp, source) " +
               "VALUES (:location, :temperature, :humidity, :pressure, :timestamp, :source)")
    void insertWeatherData(@BindBean WeatherData weatherData);
    
    @SqlQuery("SELECT * FROM weather_data WHERE location = :location ORDER BY timestamp DESC LIMIT 1")
    Optional<WeatherData> getCurrentWeather(@Bind("location") String location);
    
    @SqlQuery("SELECT * FROM weather_data WHERE location = :location " +
              "AND timestamp >= :startDate AND timestamp <= :endDate " +
              "ORDER BY timestamp DESC")
    List<WeatherData> getHistoricalWeather(@Bind("location") String location, 
                                          @Bind("startDate") Date startDate, 
                                          @Bind("endDate") Date endDate);
    
    @SqlQuery("SELECT DISTINCT location FROM weather_data ORDER BY location")
    List<String> getAllLocations();
    
    @SqlUpdate("DELETE FROM weather_data WHERE timestamp < :cutoffDate")
    int deleteOldData(@Bind("cutoffDate") Date cutoffDate);
}
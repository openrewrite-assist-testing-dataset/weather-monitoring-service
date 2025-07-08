package com.weather.collector;

import com.weather.collector.service.WeatherDataCollector;
import com.weather.collector.scheduler.CollectionScheduler;

public class DataCollectorMain {
    public static void main(String[] args) {
        WeatherDataCollector collector = new WeatherDataCollector();
        CollectionScheduler scheduler = new CollectionScheduler(collector);
        
        Runtime.getRuntime().addShutdownHook(new Thread(scheduler::shutdown));
        
        scheduler.start();
    }
}
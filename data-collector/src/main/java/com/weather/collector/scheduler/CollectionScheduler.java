package com.weather.collector.scheduler;

import com.weather.collector.service.WeatherDataCollector;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CollectionScheduler {
    private final WeatherDataCollector collector;
    private final ScheduledExecutorService scheduler;
    
    public CollectionScheduler(WeatherDataCollector collector) {
        this.collector = collector;
        this.scheduler = Executors.newScheduledThreadPool(1);
    }
    
    public void start() {
        System.out.println("Starting weather data collection scheduler...");
        
        // Collect data every 5 minutes
        scheduler.scheduleAtFixedRate(() -> {
            try {
                collector.collectAndStore();
            } catch (Exception e) {
                System.err.println("Error in scheduled data collection: " + e.getMessage());
            }
        }, 0, 5, TimeUnit.MINUTES);
        
        // Keep the main thread alive
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void shutdown() {
        System.out.println("Shutting down weather data collection scheduler...");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
        collector.shutdown();
    }
}
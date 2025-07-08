package com.weather.api;

import com.weather.api.config.WeatherApiConfiguration;
import com.weather.api.health.DatabaseHealthCheck;
import com.weather.api.resources.WeatherResource;
import com.weather.api.auth.JwtAuthFilter;
import com.weather.api.auth.ApiKeyAuthFilter;
import com.weather.api.db.WeatherDAO;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.container.ContainerRequestFilter;
import java.util.List;

public class WeatherApiApplication extends Application<WeatherApiConfiguration> {
    
    public static void main(String[] args) throws Exception {
        new WeatherApiApplication().run(args);
    }

    @Override
    public String getName() {
        return "weather-api";
    }

    @Override
    public void initialize(Bootstrap<WeatherApiConfiguration> bootstrap) {
        // Legacy initialization pattern
    }

    @Override
    public void run(WeatherApiConfiguration configuration, Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        
        final WeatherDAO weatherDAO = jdbi.onDemand(WeatherDAO.class);
        
        // Deprecated auth chaining pattern
        final ContainerRequestFilter jwtFilter = new JwtAuthFilter.Builder<String>()
            .setAuthenticator(new com.weather.api.auth.JwtAuthenticator(configuration.getJwtSecret()))
            .setPrefix("Bearer")
            .buildAuthFilter();
            
        final ContainerRequestFilter apiKeyFilter = new ApiKeyAuthFilter.Builder<String>()
            .setAuthenticator(new com.weather.api.auth.ApiKeyAuthenticator(configuration.getApiKeys()))
            .setPrefix("ApiKey")
            .buildAuthFilter();
            
        final ChainedAuthFilter<String> chainedAuthFilter = new ChainedAuthFilter<>(
            List.of(jwtFilter, apiKeyFilter));
            
        environment.jersey().register(new AuthDynamicFeature(chainedAuthFilter));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(String.class));
        
        // Register resources
        environment.jersey().register(new WeatherResource(weatherDAO));
        
        // Register health checks
        environment.healthChecks().register("database", new DatabaseHealthCheck(jdbi));
    }
}
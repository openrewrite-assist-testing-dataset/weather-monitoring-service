package com.weather.api;

import com.weather.api.config.WeatherApiConfiguration;
import com.weather.api.health.DatabaseHealthCheck;
import com.weather.api.resources.WeatherResource;
import com.weather.api.auth.User;
import com.weather.api.db.WeatherDAO;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;


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

        // Configure authentication using standard Dropwizard approach
        environment.jersey().register(new AuthDynamicFeature(
            new io.dropwizard.auth.basic.BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new com.weather.api.auth.ApiKeyAuthenticator(configuration.getApiKeys()))
                .setRealm("weather-api")
                .buildAuthFilter()));

        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        // Register resources
        environment.jersey().register(new WeatherResource(weatherDAO));

        // Register health checks
        environment.healthChecks().register("database", new DatabaseHealthCheck(jdbi));
    }
}
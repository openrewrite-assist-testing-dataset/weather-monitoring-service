package com.weather.api.resources;

import com.weather.api.db.WeatherDAO;
import com.weather.common.model.WeatherData;
import com.weather.common.util.DateUtils;
import io.dropwizard.auth.Auth;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Path("/api/v1/weather")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WeatherResource {
    
    private final WeatherDAO weatherDAO;
    
    public WeatherResource(WeatherDAO weatherDAO) {
        this.weatherDAO = weatherDAO;
    }
    
    @GET
    @Path("/current/{location}")
    public Response getCurrentWeather(@Auth String user,
                                    @PathParam("location") @NotNull String location,
                                    @Context UriInfo uriInfo) {
        Optional<WeatherData> weatherData = weatherDAO.getCurrentWeather(location);
        
        if (weatherData.isPresent()) {
            return Response.ok(weatherData.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\": \"No weather data found for location: " + location + "\"}")
                .build();
        }
    }
    
    @GET
    @Path("/historical/{location}")
    public Response getHistoricalWeather(@Auth String user,
                                       @PathParam("location") @NotNull String location,
                                       @QueryParam("startDate") String startDateStr,
                                       @QueryParam("endDate") String endDateStr,
                                       @Context UriInfo uriInfo) {
        Date startDate = startDateStr != null ? DateUtils.parseDate(startDateStr) : 
                        new Date(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L));
        Date endDate = endDateStr != null ? DateUtils.parseDate(endDateStr) : new Date();
        
        List<WeatherData> historicalData = weatherDAO.getHistoricalWeather(location, startDate, endDate);
        
        return Response.ok(historicalData).build();
    }
    
    @POST
    @Path("/data")
    public Response submitWeatherData(@Auth String user,
                                    @Valid @NotNull WeatherData weatherData,
                                    @Context UriInfo uriInfo) {
        try {
            weatherDAO.insertWeatherData(weatherData);
            return Response.status(Response.Status.CREATED)
                .entity("{\"message\": \"Weather data submitted successfully\"}")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\": \"Failed to submit weather data: " + e.getMessage() + "\"}")
                .build();
        }
    }
    
    @GET
    @Path("/locations")
    public Response getAllLocations(@Auth String user,
                                  @Context UriInfo uriInfo) {
        List<String> locations = weatherDAO.getAllLocations();
        return Response.ok(locations).build();
    }
}
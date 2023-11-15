package com.example.meteo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherServices {
    @GET("forecast?q=Annecy&appid=e075a0b59517e88cc46940bb262add13&units=metric")
    Call<ForecastData> getForcast();
    @GET("forecast")
    Call<ForecastData> getForecastForCity(
            @Query("q") String cityName,
            @Query("appid") String apiKey,
            @Query("units") String metric
    );
    @GET("forecast")
    Call<ForecastData> getFiveDayForecast(
            @Query("q") String cityName,
            @Query("appid") String apiKey,
            @Query("units") String metric
    );
}
package com.example.meteo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ForecastData {
    @SerializedName("list")
    private ArrayList<Forecast>forecasts;

    @SerializedName("city")
    private City city;


    public ArrayList<Forecast> getForecasts() {
        return forecasts;
    }

    public City getCity() {
        return city;
    }
}


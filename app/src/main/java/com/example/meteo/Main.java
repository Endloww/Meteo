package com.example.meteo;

import com.google.gson.annotations.SerializedName;

public class Main {
    @SerializedName("temp")
    private double temperature;

    @SerializedName("temp_min")
    private double tempMin;

    @SerializedName("temp_max")
    private double tempMax;

    public double getTemperature() {
        return temperature;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }
}

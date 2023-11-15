package com.example.meteo;

import com.google.gson.annotations.SerializedName;

public class Forecast {

    @SerializedName("dt")
    private int datetime;

    @SerializedName("main")
    private Main main;

    public int getDatetime() {
        return datetime;
    }

    public Main getMain() {
        return main;
    }
}

package com.example.meteo;

import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }
}

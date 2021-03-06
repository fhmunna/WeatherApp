package com.example.alexander.weatherapp.data.network.models.places;

import com.google.gson.annotations.SerializedName;

public class Prediction {

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("description")
    private String description;

    @Override
    public String toString() {
        return description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

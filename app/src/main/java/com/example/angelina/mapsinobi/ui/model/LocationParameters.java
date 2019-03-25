package com.example.angelina.mapsinobi.ui.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class LocationParameters {
    @PrimaryKey
    private long id;
    private double lat;
    private double lng;
    private String time;


    public LocationParameters(long id, double lat, double lng, String time) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

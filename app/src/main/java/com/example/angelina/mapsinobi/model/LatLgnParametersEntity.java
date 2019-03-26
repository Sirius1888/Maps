package com.example.angelina.mapsinobi.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class LatLgnParametersEntity {

    @PrimaryKey(autoGenerate = false)
    private long locationId;
    private double lat;
    private double lng;
    private String time;

//    public LatLgnParametersEntity(long locationId, double lat, double lng, String time) {
//        this.locationId = locationId;
//        this.lat = lat;
//        this.lng = lng;
//        this.time = time;
//    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
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

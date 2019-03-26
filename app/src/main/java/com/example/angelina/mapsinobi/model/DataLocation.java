package com.example.angelina.mapsinobi.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class DataLocation {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private int routeNumber;
    private long locationParamenters;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }

    public long getLocationParamenters() {
        return locationParamenters;
    }

    public void setLocationParamenters(long locationParamenters) {
        this.locationParamenters = locationParamenters;
    }
}

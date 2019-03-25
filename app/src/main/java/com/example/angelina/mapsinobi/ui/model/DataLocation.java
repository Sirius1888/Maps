package com.example.angelina.mapsinobi.ui.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

@Entity
public class DataLocation {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private int routeNumber;
    private long locationParamenters;

}

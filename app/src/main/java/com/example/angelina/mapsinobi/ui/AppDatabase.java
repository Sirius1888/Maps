package com.example.angelina.mapsinobi.ui;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.location.Location;

import com.example.angelina.mapsinobi.ui.database.LocationDao;
import com.example.angelina.mapsinobi.ui.model.DataLocation;
import com.example.angelina.mapsinobi.ui.model.LocationParameters;

@Database(entities = {
        DataLocation.class,
        LocationParameters.class}, version = 1, exportSchema = true)
public abstract class AppDatabase extends RoomDatabase {


    private static AppDatabase INSTANCE;
    public abstract LocationDao locationModel();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "location")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }


}

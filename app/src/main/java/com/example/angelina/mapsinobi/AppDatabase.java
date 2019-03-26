package com.example.angelina.mapsinobi;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.angelina.mapsinobi.database.LocationDao;
import com.example.angelina.mapsinobi.model.LatLgnParametersEntity;
import com.example.angelina.mapsinobi.model.LocationEntity;

@Database(entities = {
        LocationEntity.class,
        LatLgnParametersEntity.class}, version = 1, exportSchema = true)
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

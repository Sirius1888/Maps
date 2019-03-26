package com.example.angelina.mapsinobi.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.angelina.mapsinobi.model.DataLocation;

import java.util.List;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM datalocation")
    List<DataLocation> getAllDataLocation();

    @Query("SELECT * FROM datalocation where id = :id")
    DataLocation getDataLocation(long id);

    @Insert
    void addNewPlace(DataLocation myLoc);


}

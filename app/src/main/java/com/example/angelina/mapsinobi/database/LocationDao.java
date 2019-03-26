package com.example.angelina.mapsinobi.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.angelina.mapsinobi.model.LatLgnParametersEntity;
import com.example.angelina.mapsinobi.model.LocationEntity;

import java.util.List;

import butterknife.OnClick;

@Dao
public interface LocationDao {

    @Query("SELECT * FROM LocationEntity")
    List<LocationEntity> getAllHistory();

    @Query("SELECT * FROM LocationEntity where id = :id")
    LocationEntity getStory(long id);

    @Query("SELECT * FROM LatLgnParametersEntity where locationId = :id")
    List<LatLgnParametersEntity> getAllLatLgnParameters(long id);
    @Insert
    void addNewRouter(LocationEntity myLoc);

    @Insert
    void addAllPoint(List<LatLgnParametersEntity> latLgnParameterEntities);

    @Insert
    void addPoint(LatLgnParametersEntity latLgnParametersEntity);

    @Query("SELECT max(id) FROM LocationEntity")
    long getLastRoute();


}

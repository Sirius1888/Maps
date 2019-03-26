package com.example.angelina.mapsinobi.ui.activity.main.presenter;

import com.example.angelina.mapsinobi.model.LatLgnParametersEntity;

import java.util.List;

public interface MapsPresenter {

    void startRouterMap();
    void insertLocationData(long locationId);

    void addPointList(List<LatLgnParametersEntity> list);
}

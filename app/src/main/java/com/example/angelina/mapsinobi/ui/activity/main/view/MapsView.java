package com.example.angelina.mapsinobi.ui.activity.main.view;

import android.location.Location;

import com.example.angelina.mapsinobi.BaseView;
import com.example.angelina.mapsinobi.model.LatLgnParametersEntity;
import com.example.angelina.mapsinobi.ui.activity.main.presenter.MapsPresenter;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface MapsView extends BaseView<MapsPresenter> {

    void showLocation(Location location);

    String formatLocation(Location location);
    void checkEnabled();

    void showToast(String message);

    void drawRoute(List<LatLng> lpLiist);
    void addPoints(List<LatLgnParametersEntity> list);
}

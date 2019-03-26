package com.example.angelina.mapsinobi.ui.activity.main.view;

import android.location.Location;
import android.location.LocationManager;

import com.example.angelina.mapsinobi.BaseView;
import com.example.angelina.mapsinobi.ui.activity.main.presenter.MapsPresenter;

public interface MapsView extends BaseView<MapsPresenter> {

    void showLocation(Location location);

    String formatLocation(Location location);
    void checkEnabled();

    void showToast(String message);

}

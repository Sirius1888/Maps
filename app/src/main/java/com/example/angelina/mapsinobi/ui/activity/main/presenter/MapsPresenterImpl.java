package com.example.angelina.mapsinobi.ui.activity.main.presenter;

import com.example.angelina.mapsinobi.database.LocationDao;
import com.example.angelina.mapsinobi.ui.activity.main.view.MapsView;

public class MapsPresenterImpl implements MapsPresenter {
    private MapsView view;
    private final LocationDao locationDao;

    public MapsPresenterImpl(MapsView view, LocationDao locationDao) {
        this.view = view;
        this.view.setPresenter(this);
        this.locationDao = locationDao;
    }
}

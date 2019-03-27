package com.example.angelina.mapsinobi.ui.activity.history.presenter;

import com.example.angelina.mapsinobi.database.LocationDao;
import com.example.angelina.mapsinobi.model.LocationEntity;
import com.example.angelina.mapsinobi.ui.activity.history.view.HistoryContract;

import java.util.List;

public class HistoryPresenterImpl implements HistoryPresenter {
    private HistoryContract.View view;
    private final LocationDao locationDao;
    // Создаем экземпляр


    public HistoryPresenterImpl(HistoryContract.View view, LocationDao locationDao) {
        this.view = view;
        this.view.setPresenter(this);
        this.locationDao = locationDao;
    }


    @Override
    public void showAllHistory(){
        if (view != null) {
            List<LocationEntity> historyModel = locationDao.getAllHistory();
            if (historyModel != null && historyModel.size() > 0) {
                view.showHistory(historyModel);
                view.showRecyclerView();
            } else {
                view.showHistory(historyModel);
                view.showEmptyMessage();
            }
        }
    }



}

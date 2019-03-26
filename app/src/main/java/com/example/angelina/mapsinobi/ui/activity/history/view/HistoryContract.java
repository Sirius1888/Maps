package com.example.angelina.mapsinobi.ui.activity.history.view;


import com.example.angelina.mapsinobi.BaseView;
import com.example.angelina.mapsinobi.model.LocationEntity;
import com.example.angelina.mapsinobi.ui.activity.history.presenter.HistoryPresenter;

import java.util.List;

public interface HistoryContract  {

    interface View extends BaseView<HistoryPresenter> {
        void showToast(String message);
        void showEmptyMessage();

        void showRecyclerView();

        void showHistory(List<LocationEntity> historyModel);
    }

    interface OnItemClickListener {
        void clickItem(LocationEntity locationEntity);
        void clickLongItem(LocationEntity locationEntity);
    }

}

package com.example.angelina.mapsinobi.ui.activity.main.presenter;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.example.angelina.mapsinobi.database.LocationDao;
import com.example.angelina.mapsinobi.ui.activity.main.view.MapsView;
import com.example.angelina.mapsinobi.utils.PrefUtil;

public class MapsPresenterImpl implements MapsPresenter {
    private MapsView view;
    private final LocationDao locationDao;
    // Создаем экземпляр


    public MapsPresenterImpl(MapsView view, LocationDao locationDao) {
        this.view = view;
        this.view.setPresenter(this);
        this.locationDao = locationDao;
    }




    @Override
    public void startRouterMap() {
        RepeatableAddPoint longTask = new RepeatableAddPoint();
        longTask.execute();
    }

    public void addRouter() {

    }

    public void addNewPoint() {

    }


    class RepeatableAddPoint extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... noargs) {
            while(PrefUtil.getTracking()){
                Log.i("AsyncTask_doInBack", "DO");
                SystemClock.sleep(1000);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.showToast("Начало трекинга");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            view.showToast("Конец трекинга");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

    }




}

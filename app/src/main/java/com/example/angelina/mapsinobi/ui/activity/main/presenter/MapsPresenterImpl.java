package com.example.angelina.mapsinobi.ui.activity.main.presenter;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.example.angelina.mapsinobi.database.LocationDao;
import com.example.angelina.mapsinobi.model.LatLgnParametersEntity;
import com.example.angelina.mapsinobi.model.LocationEntity;
import com.example.angelina.mapsinobi.ui.activity.main.view.MapsView;
import com.example.angelina.mapsinobi.utils.PrefUtil;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapsPresenterImpl implements MapsPresenter {
    private MapsView view;
    private final LocationDao locationDao;
    private static long locationId = 0;

    // Создаем экземпляр


    public MapsPresenterImpl(MapsView view, LocationDao locationDao) {
        this.view = view;
        this.view.setPresenter(this);
        this.locationDao = locationDao;
    }

    @Override
    public void startRouterMap() {
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setNameRoute("temp ");
        locationDao.addNewRouter(locationEntity);
        locationId = locationDao.getLastRoute();
        new RepeatableAddPoint().execute();



    }

    public void addRouter() {

    }

    public void addNewPoint() {

    }

    @Override
    public void addPointList(List<LatLgnParametersEntity> list) {
        locationDao.addAllPoint(list);
    }

    @Override
    public void insertLocationData(long locationId) {
        List<LatLng> list = new ArrayList<LatLng>();
        List<LatLgnParametersEntity> lpLiist = locationDao.getAllLatLgnParameters(locationId);
        if (lpLiist.size() > 0) {
            for (int i = 0; i < lpLiist.size(); i++) {
                list.add(new LatLng(lpLiist.get(i).getLat(), lpLiist.get(i).getLng()));
            }
        }
        view.drawRoute(list);
    }

    private void update(List<LatLgnParametersEntity> latLgnParametersEntityList) {
        locationDao.addAllPoint(latLgnParametersEntityList);
    }

    class RepeatableAddPoint extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... noargs) {
            List<LatLgnParametersEntity> latLgnParameterEntities = new ArrayList<>();

            while(PrefUtil.getTracking()){
                Log.i("AsyncTask_doInBack", "DO ");

                LatLgnParametersEntity lp1 = new LatLgnParametersEntity();
                lp1.setLocationId(locationId);
                lp1.setTime(PrefUtil.getTime());
                lp1.setLat(Double.parseDouble(PrefUtil.getLat()));
                lp1.setLng(Double.parseDouble(PrefUtil.getLon()));
                latLgnParameterEntities.add(lp1);
                SystemClock.sleep(1000);
                if (!PrefUtil.getTracking())
//                    new Handler().post(() -> locationDao.addAllPoint(latLgnParameterEntities));
                    new Thread(() -> locationDao.addAllPoint(latLgnParameterEntities));

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
            super.onPostExecute(aVoid);
            view.showToast("Конец трекинга");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

    }


//    class InsertToDatabase extends AsyncTask<Void, Integer, Void> {
//        @Override
//        protected Void doInBackground(Void... noargs) {
//            List<LatLgnParametersEntity> latLgnParameters = new ArrayList<>();
//
//            while(PrefUtil.getTracking()){
//                Log.i("AsyncTask_doInBack", "DO ");
//
//                LatLgnParametersEntity lp1 = new LatLgnParametersEntity();
//                lp1.setLocationId(locationId);
//                lp1.setTime(PrefUtil.getTime());
//                lp1.setLat(Double.parseDouble(PrefUtil.getLat()));
//                lp1.setLng(Double.parseDouble(PrefUtil.getLon()));
//                latLgnParameters.add(lp1);
//                SystemClock.sleep(1000);
//                if (!PrefUtil.getTracking())
//                    new Thread(() -> locationDao.addAllPoint(latLgnParameters));
//
//            }
//
//            return null;
//        }
//
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            view.showToast("Начало трекинга");
//
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            view.showToast("Конец трекинга");
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//
//        }
//
//    }







}

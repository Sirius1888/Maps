package com.example.angelina.mapsinobi.ui.activity.main.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.angelina.mapsinobi.R;
import com.example.angelina.mapsinobi.AppDatabase;
import com.example.angelina.mapsinobi.model.LatLgnParametersEntity;
import com.example.angelina.mapsinobi.ui.activity.history.view.HistoryActivity;
import com.example.angelina.mapsinobi.ui.activity.main.presenter.MapsPresenter;
import com.example.angelina.mapsinobi.ui.activity.main.presenter.MapsPresenterImpl;
import com.example.angelina.mapsinobi.utils.PrefUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.angelina.mapsinobi.utils.IntenetConnection.checkConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsView {

    @BindView(R.id.close_router)        ImageButton closeStoryBtn;
    @BindView(R.id.increase_zoom_btn)   ImageButton increaseMapBtn;
    @BindView(R.id.decrease_zoom_btn)   ImageButton decreaseMapBtn;
    @BindView(R.id.location_btn)        ImageButton locationBtn;
    @BindView(R.id.history_btn)         ImageButton historyBtn;
    @BindView(R.id.start_router_btn)    ImageButton startRouterBtn;


    private GoogleMap mMap;
    private MapsPresenter presenter;

    boolean isPressed = false;

    private LocationManager locationManager;
    StringBuilder GPS = new StringBuilder();
    StringBuilder NETWORK = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        AppDatabase db = AppDatabase.getDatabase(getApplication());
        presenter = new MapsPresenterImpl(this, db.locationModel());

//        mMap.clear();
        getLocation();
        PrefUtil.setIsHistory(false);

//        checkPermInternet()
    }

    private void checkPermInternet() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {}
                        @Override public void onPermissionDenied(PermissionDeniedResponse response) {}
                        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                    }).check();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {recreate();}
                        @Override public void onPermissionDenied(PermissionDeniedResponse response) {recreate();}
                        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                    }).check();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();
    }

    @Override
    public void addPoints(List<LatLgnParametersEntity> list) {
        presenter.addPointList(list);
    }

    private void getLocation() {
        if (!PrefUtil.getIsHistory()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 5, 5, locationListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 1000 * 5, 5,
                    locationListener
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i("LocationChanged", "change");
            if (!PrefUtil.getIsHistory()){
                showLocation(location);
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i("LocationOnStatusChanged", "statusChanged");

            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Log.i("LocationChanged", "change");
            if (!PrefUtil.getIsHistory()){
                showLocation(locationManager.getLastKnownLocation(provider));
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
//            mMap.clear();
            showToast("GPS или интернет отключены");
//            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    };

    @Override
    public void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            showToast(formatLocation(location));
            showNewPosition(location);
        } else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            showToast(formatLocation(location));
            showNewPosition(location);

        }

    }

    public void showNewPosition(Location location) {
//        PrefUtil.setLat(String.valueOf(location.getLatitude()));
//        PrefUtil.setLon(String.valueOf((location.getLongitude())));

        Random random = new Random();
        double r1 = random.nextInt(25) + 1;
        double r2 = random.nextInt(25) + 1;
        PrefUtil.setLat(String.valueOf(location.getLatitude() + r1));
        PrefUtil.setLon(String.valueOf((location.getLongitude() + r2)));

        PrefUtil.setTime(new Date(location.getTime()));
        mMap.clear();
        onMapReady(mMap);

        showToast("Координаты:  lat = " + PrefUtil.getLat() + " lon = " + PrefUtil.getLon() + " time = " + PrefUtil.getTime());

    }

    @SuppressLint("DefaultLocale")
    @Override
    public String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Координаты:  lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(location.getTime()));

    }

    @Override
    public void checkEnabled() {
        showToast("Gps включено: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        showToast("Network включено: " + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(Double.parseDouble(PrefUtil.getLat()),
                Double.parseDouble(PrefUtil.getLon()));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Ваше местоположение"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(PrefUtil.getZoom()));

    }

    @Override
    public void setPresenter(MapsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void drawRoute(List<LatLng> lpList) {
//        PolylineOptions polylineOptions = new PolylineOptions().addAll(lpList).color(Color.YELLOW).width(1);
//        mMap.addPolyline(polylineOptions);
        PolylineOptions polylineOptions = new PolylineOptions();
        int size = lpList.size();
        for (int i = 0; i < size; i++) {
            polylineOptions.add(new LatLng(lpList.get(i).latitude, lpList.get(i).longitude));
        }
        polylineOptions.color(Color.MAGENTA).width(10);
        mMap.addPolyline(polylineOptions);

        hideButtons();
        PrefUtil.setIsHistory(true);

    }

    private void hideButtons() {
        closeStoryBtn.setVisibility(View.VISIBLE);
        locationBtn.setVisibility(View.INVISIBLE);
        historyBtn.setVisibility(View.INVISIBLE);
        startRouterBtn.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.close_router)
    public void close(View view) {
        showButtons();
        mMap.clear();
        getLocation();
        PrefUtil.setIsHistory(false);
    }

    private void showButtons() {
        closeStoryBtn.setVisibility(View.INVISIBLE);
        locationBtn.setVisibility(View.VISIBLE);
        historyBtn.setVisibility(View.VISIBLE);
        startRouterBtn.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.increase_zoom_btn, R.id.decrease_zoom_btn})
    public void zoomIn(View view) {
        switch (view.getId()) {
            case R.id.increase_zoom_btn:
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                PrefUtil.setZoom(PrefUtil.getZoom()+1);
                break;
            case R.id.decrease_zoom_btn:
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
                PrefUtil.setZoom(PrefUtil.getZoom()-1);
                break;
        }
    }


    @OnClick(R.id.location_btn)
    public void findYourLocation() {
        mMap.clear();
        onMapReady(mMap);
        getLocation();
//        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @OnClick(R.id.history_btn)
    public void showHistory() {
        if (PrefUtil.getTracking()) {
            showToast("Сначала завершите маршрут!");
        } else {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivityForResult(intent, 1);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == 1){
            if(resultCode == Activity.RESULT_OK) {
                String routeId = data.getStringExtra("intentId");
                presenter.getLocationRoute(PrefUtil.getIdRoute());
            } else {
                showToast("Произошла ошибка с выбором маршрута");
            }
        }
    }

    @OnClick(R.id.start_router_btn)
    public void startRoute() {
        if (checkConnection(getApplicationContext())) {
            if (PrefUtil.getTracking())
                PrefUtil.setTracking(false);
            else {
                PrefUtil.setTracking(true);
                presenter.startRouterMap();
            }
        } else {
            showToast("Отсутствует интернет соединение");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PrefUtil.setZoom(15);
    }

}

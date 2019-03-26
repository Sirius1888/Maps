package com.example.angelina.mapsinobi.ui.activity.main.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.angelina.mapsinobi.R;
import com.example.angelina.mapsinobi.AppDatabase;
import com.example.angelina.mapsinobi.ui.activity.main.presenter.MapsPresenter;
import com.example.angelina.mapsinobi.ui.activity.main.presenter.MapsPresenterImpl;
import com.example.angelina.mapsinobi.utils.PrefUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsView {

    @BindView(R.id.increase_zoom_btn)
    ImageButton increaseMapBtn;
    @BindView(R.id.decrease_zoom_btn)
    ImageButton decreaseMapBtn;

    @BindView(R.id.location_btn)
    ImageButton locationBtn;

    @BindView(R.id.history_btn)
    ImageButton historyBtn;
    @BindView(R.id.start_router_btn)
    ImageButton startRouterBtn;


    private GoogleMap mMap;
    private MapsPresenter presenter;
    public static double lat = 0;
    public static double lon = 0;
    boolean isPressed = false;

    private LocationManager locationManager;
    StringBuilder GPS = new StringBuilder();
    StringBuilder NETWORK = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        AppDatabase db = AppDatabase.getDatabase(getApplication());
        presenter = new MapsPresenterImpl(this, db.locationModel());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation();

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            showToast(formatLocation(location));
        } else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            showToast(formatLocation(location));
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String formatLocation(Location location) {
        if (location == null)
            return "";
        Random random = new Random();
        double r1 = random.nextInt(50) + 1;
        double r2 = random.nextInt(50) + 1;
        PrefUtil.setLat(String.valueOf(location.getLatitude() + r1));
        PrefUtil.setLon(String.valueOf((location.getLongitude() + r2)));
        PrefUtil.setTime(new Date(location.getTime()));
        mMap.clear();
        onMapReady(mMap);
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

    }

    @Override
    public void setPresenter(MapsPresenter presenter) {
        this.presenter = presenter;
    }


    @OnClick({R.id.increase_zoom_btn, R.id.decrease_zoom_btn, R.id.location_btn,
            R.id.history_btn, R.id.start_router_btn})
    public void actionButton(View view) {
        switch (view.getId()) {
            case R.id.increase_zoom_btn:

                break;
            case R.id.decrease_zoom_btn:

                break;
            case R.id.location_btn:
                mMap.clear();
                onMapReady(mMap);
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;
            case R.id.history_btn:
                showToast("ghbdtn");
                break;
            case R.id.start_router_btn:

                if (PrefUtil.getTracking())
                    PrefUtil.setTracking(false);
                else {
                    PrefUtil.setTracking(true);
                    presenter.startRouterMap();
                }
                break;
        }
    }
}

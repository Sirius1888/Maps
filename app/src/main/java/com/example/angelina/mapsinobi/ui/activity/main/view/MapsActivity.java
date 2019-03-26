package com.example.angelina.mapsinobi.ui.activity.main.view;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.angelina.mapsinobi.R;
import com.example.angelina.mapsinobi.AppDatabase;
import com.example.angelina.mapsinobi.ui.activity.main.presenter.MapsPresenter;
import com.example.angelina.mapsinobi.ui.activity.main.presenter.MapsPresenterImpl;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.OnClick;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapsView {

    @BindView(R.id.increase_zoom_btn) Button increaseMapBtn;
    @BindView(R.id.decrease_zoom_btn) Button decreaseMapBtn;

    @BindView(R.id.location_btn) Button locationBtn;

    @BindView(R.id.history_btn) Button historyBtn;
    @BindView(R.id.start_router_btn) Button startRouterBtn;


    private GoogleMap mMap;
    private MapsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        AppDatabase db = AppDatabase.getDatabase(getApplication());
        presenter = new MapsPresenterImpl(this, db.locationModel());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
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

                break;
            case R.id.history_btn:

                break;
            case R.id.start_router_btn:

                break;
        }
    }
}

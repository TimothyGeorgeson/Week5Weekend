package com.example.consultants.week5weekend.view;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.consultants.week5weekend.R;
import com.example.consultants.week5weekend.utils.LatLngCallback;
import com.example.consultants.week5weekend.client.OkhttpHelper;
import com.example.consultants.week5weekend.model.data.LocationResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String TAG = MapsActivity.class.getSimpleName() + "_TAG";

    private OkhttpHelper okhttpHelper;
    private GoogleMap mMap;
    private Double lat;
    private Double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //default values in case of network errors
        lat = -34D;
        lng = 151D;

        //get location from intent
        String location = getIntent().getStringExtra("location");
        //build URL with okhttp using location
        okhttpHelper = new OkhttpHelper(location);

        //enqueue to do network call on separate thread, and get lat/lng results
        okhttpHelper.enqueue(new LatLngCallback() {
            @Override
            public void onSuccess(LocationResponse locationResponse) {
                if (locationResponse.getResults().size() > 0) {
                    lat = locationResponse.getResults().get(0).getGeometry().getLocation().getLat();
                    lng = locationResponse.getResults().get(0).getGeometry().getLocation().getLng();
                    Log.d(TAG, "onSuccess: " + lat + "  " + lng);

                    //getMapAsync must be run on main thread, so I use runOnUiThread
                    MapsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            prepareMap();
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "onFailure: " + error);
            }
        });
    }

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    private void prepareMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker to location
        LatLng location = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(location).title("Location"));

        //moves camera and sets zoom level
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);
        mMap.animateCamera(zoom);
    }
}

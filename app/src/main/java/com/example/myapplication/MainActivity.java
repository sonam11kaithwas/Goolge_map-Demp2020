package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private static int LOCATION_REQUEST_CODE = 100;
    GoogleMap googleMap;
    boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private double latitude = 0.0, longitude = 0.0;
    private LocationCallback mLocationCallback;
    private CustomWindowModel customWindowModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(2000);


        ask_User_RunTime_PerMission();

        /*****get current location update callback*/
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //   txtLocation.setText(String.format("Latituted=" + latitude + "\n Longituted=" + longitude));
                    }
                }
            }
        };


        /**Load Google map**/
        SupportMapFragment layout = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        layout.getMapAsync(this);
    }

    /*****/
    private void ask_User_RunTime_PerMission() {
        if (Build.VERSION.SDK_INT >= M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_REQUEST_CODE);
            } else {
                // already permission granted
                get_Last_LocationUpdate();
            }
        } else {
            get_Last_LocationUpdate();
        }
    }

    private void get_Last_LocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                addMarkerOnMap();
                onStartLocationUpdate();
                // txtLocation.setText(String.format("Latituted=" + latitude + "\n  Longituted=" + longitude));

            }

        });
    }


    @Override
    public void onMapReady(GoogleMap google_Map) {
        googleMap = google_Map;
        googleMap.setOnInfoWindowClickListener(this::onInfoWindowClick);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Your marker", Toast.LENGTH_SHORT).show();
    }

    /**
     * add single marker on map with current lat lng
     **/
    void addMarkerOnMap() {
        LatLng sydney = new LatLng(latitude, longitude);
        Marker marker = googleMap.addMarker(new MarkerOptions().position(sydney).title(getAddressfromlatlng()));
        //  googleMap.addMarker(new MarkerOptions().position(sydney).title(getAddressfromlatlng()));

        CustomMarkerWindowAdpter customInfoWindow = new CustomMarkerWindowAdpter(this);
        googleMap.setInfoWindowAdapter(customInfoWindow);
        marker.setTag(customWindowModel);
        marker.showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    /***get address from lag/lng **/
    private String getAddressfromlatlng() {
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); //
        customWindowModel = new CustomWindowModel(address, city, state, country, latitude, longitude);
        return address + " " + city + " " + "\n" + state + " " + country;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("PerMission Granted", "----->>>>>");
                    // Toast.makeText(this, "PerMission Granted.......", Toast.LENGTH_SHORT).show();
                    get_Last_LocationUpdate();

                } else {
                    Log.e("PerMission Denied", "----->>>>>");
                    //txt_loc.setText("Location not available !!!");
                    Toast.makeText(this, "Location permission granted required for latituted & longituted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void onStartLocationUpdate() {
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

        //permission required
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onStopLocationUpdate();
    }

    @Override
    public void onStart() {
        super.onStart();
        // onStartLocationUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onStartLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void onStopLocationUpdate() {
        if (mFusedLocationClient != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
}

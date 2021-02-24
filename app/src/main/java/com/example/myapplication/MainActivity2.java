package com.example.myapplication;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap googleMap;
    ArrayList<MarkerLatLng> markersArray;
    private CustomWindowModel customWindowModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Load Google map**/
        SupportMapFragment layout = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        layout.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        addLatLng();
        for (int i = 0; i < markersArray.size(); i++) {
            createMarker(markersArray.get(i).getLat(), markersArray.get(i).getLng());
        }
    }

    private void addLatLng() {
        markersArray = new ArrayList<MarkerLatLng>();
        markersArray.add(new MarkerLatLng(22.713882, 75.894264));
        markersArray.add(new MarkerLatLng(22.7508411, 75.8935361));
        markersArray.add(new MarkerLatLng(22.7336945, 75.8879226));
        markersArray.add(new MarkerLatLng(22.6926379, 75.8654135));
        markersArray.add(new MarkerLatLng(22.7195314, 75.8936644));
    }

    private CustomWindowModel convertLatLntToAdr(double latitude, double longitude) {
        //  customWindowModel=new CustomWindowModel();
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
        // return address + " " + city + " " + "\n" + state + " " + country;
        return customWindowModel;
    }

    protected Marker createMarker(double latitude, double longitude) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));

        CustomMarkerWindowAdpter customInfoWindow = new CustomMarkerWindowAdpter(this);
        googleMap.setInfoWindowAdapter(customInfoWindow);
        CustomWindowModel model = convertLatLntToAdr(latitude, longitude);
        marker.setTag(new CustomWindowModel(model.getAdr(), model.getCity(), "", "", latitude, longitude));
        marker.showInfoWindow();

        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude)));

    }

}
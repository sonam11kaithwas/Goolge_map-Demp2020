package com.example.myapplication;

/**
 * Created by Sonam-11 on 5/6/20.
 */
class MarkerLatLng {
    private double lat;
    private double lng;

    public MarkerLatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}

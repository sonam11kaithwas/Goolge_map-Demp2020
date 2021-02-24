package com.example.myapplication;

/**
 * Created by Sonam-11 on 2/6/20.
 */
public class CustomWindowModel {
    String adr;
    String city;
    String state;
    String country;
    double lat, lng;

    public String getAdr() {
        return adr;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public CustomWindowModel(String adr, String city, String state, String country, double lat, double lng) {
        this.adr = adr;
        this.city = city;
        this.state = state;
        this.country = country;
        this.lat = lat;
        this.lng = lng;
    }

    public CustomWindowModel() {
    }
}

package com.baidu.mapclient.liteapp.model;

public class NaviLatLng {

    public double latitude;
    public double longitude;

    public NaviLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return "NaviLatLng{" + "latitude=" + latitude + ", longitude=" + longitude + '}';
    }
}

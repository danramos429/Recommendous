package com.staticvillage.recommender;

/**
 * Created by joelparrish on 12/28/14.
 */
public class GeoPoint {
    private double latitude;
    private double longitude;

    public GeoPoint(){
        this(0, 0);
    }
    public GeoPoint(double latitude, double longitude){
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
}

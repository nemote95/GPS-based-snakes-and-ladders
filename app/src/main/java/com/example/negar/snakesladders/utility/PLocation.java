package com.example.negar.snakesladders.utility;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import 	java.lang.Math;

public class PLocation {
    private double boardCenterLat;
    private double boardCenterLong;

    private Location currentLocation;
    String Tag = "GPS";

    public PLocation(double centerLat,double centerLong,Location current){
        currentLocation=current;
        boardCenterLat=centerLat;
        boardCenterLong=centerLong;

    }

    //returns the Player's current Location
    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Point getRelativePx(double MetersPerPixel){
        double rto = 1/MetersPerPixel;
        double dLAT = ((boardCenterLat - currentLocation.getLatitude())/ 0.00001) * rto;
        double dLNG = -1 * ((boardCenterLong - currentLocation.getLongitude()) / 0.00001) * rto;
        int y = Math.abs((int)Math.round(dLAT));
        int x =  Math.abs((int)Math.round(dLNG));
        Point crd = new Point(x, y);
        return crd;
    }
}

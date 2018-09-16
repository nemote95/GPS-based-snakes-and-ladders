package com.example.negar.snakesladders.utility;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;

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
        //double dLAT = ((boardCenterLat - currentLocation.getLatitude())/ 0.00001) * rto;
        //double dLNG = -1 * ((boardCenterLong - currentLocation.getLongitude()) / 0.00001) * rto;
        int R= 6371;
        double x_curr = R * Math.cos(currentLocation.getLatitude()) * Math.cos(currentLocation.getLongitude());
        double y_curr = R * Math.cos(currentLocation.getLatitude()) * Math.sin(currentLocation.getLongitude());

        double x_cent = R * Math.cos(boardCenterLat) * Math.cos(boardCenterLong);
        double y_cent = R * Math.cos(boardCenterLat) * Math.sin(boardCenterLong);

        int x = Math.abs((int)Math.round((x_cent-x_curr)*320));
        int y =  Math.abs((int)Math.round((y_cent-y_curr)*320));
        Log.e("calc","x"+(x_cent-x_curr)*320+"y"+ (y_cent-y_curr)*320);
        Point crd = new Point(x, y);
        return crd;
    }
}

package com.example.negar.snakesladders.utility;

import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class PLocation extends Service implements android.location.LocationListener {
    private Location boardCenter;
    private Location currentLocation;
    String Tag = "GPS";

    //returns the Location of the center of the board
    public Location getBoardCenter() {
        return boardCenter;
    }

    //returns the Player's current Location
    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Point getRelativePx(double MetersPerPixel){
        double rto = 1/MetersPerPixel;
        double dLAT = ((boardCenter.getLatitude() - currentLocation.getLatitude())/ 0.00001) * rto;
        double dLNG = -1 * ((boardCenter.getLongitude() - currentLocation.getLongitude()) / 0.00001) * rto;
        int y = (int)Math.round(dLAT);
        int x = (int)Math.round(dLNG);
        Point crd = new Point(x, y);
        return crd;
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        // Do something with the location
        Toast.makeText(getBaseContext(),  "Location changed!", Toast.LENGTH_SHORT).show();
        Log.i("Provider: ", location.getProvider());
        Log.i("Latitude: ", String.valueOf(location.getLatitude()));
        Log.i("Longitude: ", String.valueOf(location.getLongitude()));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("onStatusChanged: ",  "Do something with the status: " + status );
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "GPS enabled", Toast.LENGTH_SHORT).show();
        Log.i("onProviderEnabled: ", "Do something with the provider-> " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getBaseContext(), "GPS disabled, please turn GPS on to continue", Toast.LENGTH_SHORT).show();
        Log.i("onProviderDisabled:", "Do something with the provider-> " + provider);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

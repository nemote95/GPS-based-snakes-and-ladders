package com.example.negar.snakesladders;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.negar.snakesladders.utility.PLocation;

public class SetBoardCenter extends Activity {

    TextView location;
    Button getLocation;
    Button go;
    PLocation GPSListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_board_center);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);

        String provider = locationManager.getBestProvider(criteria, false);
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(provider);

            GPSListener= new PLocation();

            if (location != null) {
                // add location to the location listener for location changes
                GPSListener.onLocationChanged(location);
            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

            // location updates: at least 1 meter and 500 milli seconds change
            locationManager.requestLocationUpdates(provider, 500, 1, GPSListener);
        } catch (SecurityException e) {
            Log.e("SecurityException", e.getMessage());
        }
    }

    private void setCenter(){

    }

}

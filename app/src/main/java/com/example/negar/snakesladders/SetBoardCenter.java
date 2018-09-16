package com.example.negar.snakesladders;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SetBoardCenter extends Activity implements OnClickListener {

    TextView location;
    Button getLocation;
    Button go;

    private LocationManager locationMangaer=null;
    private LocationListener locationListener=null;
    Location center=null;

    private Button btnGetLocation = null;
    private EditText editLocation = null;
    private ProgressBar pb =null;

    private static final String TAG = "Debug";
    private Boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_board_center);
        //if you want to lock screen for always Portrait mode
        setRequestedOrientation(ActivityInfo
                .SCREEN_ORIENTATION_PORTRAIT);

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);

        editLocation = (EditText) findViewById(R.id.editTextLocation);

        btnGetLocation = (Button) findViewById(R.id.btnLocation);
        btnGetLocation.setOnClickListener(this);

        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = locationMangaer.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                // add location to the location listener for location changes
                //GPSListener.onLocationChanged(location);
            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } catch (SecurityException e) {
            //dialogGPS(this.getContext()); // lets the user know there is a problem with the gps
        }

    }

    @Override
    public void onClick(View v) {
        flag = displayGpsStatus();
        if (flag) {

            pb.setVisibility(View.VISIBLE);
            locationListener = new MyLocationListener();

            try {
                Location location = locationMangaer.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locationMangaer.requestLocationUpdates(LocationManager
                        .GPS_PROVIDER, 0, 0,locationListener);
            } catch (SecurityException e) {
                //dialogGPS(this.getContext()); // lets the user know there is a problem with the gps
            }


        } else {
            alertbox("Gps Status!!", "Your GPS is: OFF");
        }

    }

    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }


    /*----------Method to create an AlertBox ------------- */
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_SECURITY_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            center=loc;
            editLocation.setText("");
            pb.setVisibility(View.INVISIBLE);
            //Toast.makeText(getBaseContext(),"Location changed : Lat: " +
                            //loc.getLatitude()+ " Lng: " + loc.getLongitude(),
                    //Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " +loc.getLongitude();
            String latitude = "Latitude: " +loc.getLatitude();
            editLocation.setText(latitude+"\n"+longitude);

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
    public void setCenter(View view){
        Intent resultIntent = new Intent(this,Board.class);
        if (center!=null){
            JSONObject centerJSON = new JSONObject();
            try {
                centerJSON.put("lat", center.getLatitude());
                centerJSON.put("long", center.getLongitude());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            resultIntent.putExtra("centerLocation", centerJSON.toString());}
        else {
            alertbox("Gps Status!!", "Your GPS is: OFF");
        }
        locationMangaer.removeUpdates(locationListener);
        locationMangaer = null;
        startActivity(resultIntent);

    }

}
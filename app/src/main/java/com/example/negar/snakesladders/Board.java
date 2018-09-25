package com.example.negar.snakesladders;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import com.example.negar.snakesladders.utility.Drawing;
import com.example.negar.snakesladders.utility.PLocation;
import org.json.JSONException;
import org.json.JSONObject;



public class Board extends AppCompatActivity implements LocationListener {
    //drawing and UI element settings
    private ImageView mImageView;
    private Drawing drawing;
    private Bitmap avatar;
    private Bitmap ladder;
    private Bitmap snake;
    ImageView diceButton;
    private TextView information;
    private View mainView;
    private int rect=0;

    //board settings and positions
    private int boardSize=5;    //get the size from indent later
    private int userTile=1;
    private int userGoal=1;

    //communicating with other activities
    static final int ROLL_Dice_REQUEST = 1;  // The request code

    //gps
    double boardLong,boardLat;
    protected LocationManager locationManager;
    protected String latitude,longitude;
    int minTimeUpdate=5000;

    //location to point settings
    private PLocation pLocation;
    double mpp=0.025;

    //synchronizing for UI
    final Handler myHandler = new Handler();

    //menu
    private DrawerLayout mDrawerLayout;

    //light management
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private float maxValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //menu layout settings
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        //getting location center
        Intent intent = getIntent();
        String location = intent.getStringExtra("centerLocation");

        try {
            JSONObject locationObj = new JSONObject(location);
            boardLat = locationObj.getDouble("lat");
            boardLong = locationObj.getDouble("long");
            pLocation=new PLocation(boardLat,boardLong,12.742*1000000,mpp);
        } catch (JSONException e) {
            Log.e("json", e.toString());
        }

        //battery management for location update requests
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level / (float)scale;

        if (batteryPct<0.3){
            minTimeUpdate=1000;
        }

        //location settings
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeUpdate, 0, this);

        //light management
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor != null) {
            // max value for light sensor
            maxValue = lightSensor.getMaximumRange();
            lightEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    float value = sensorEvent.values[0];
                    if (value/maxValue <0.5){
                    mainView.setBackgroundColor(Color.rgb(0,0,0));}
                    else{
                        mainView.setBackgroundColor(Color.rgb(255,255,255));}

                }
                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
        }
        //board display settings
        rect = ResourcesCompat.getColor(getResources(), R.color.colorRectangle, null);
        int txt = ResourcesCompat.getColor(getResources(), R.color.colorBoardText, null);
        drawing = new Drawing(rect, txt, boardSize);

        //responsive board size
        mImageView = (ImageView) findViewById(R.id.myimageview);
        int height = getResources().getSystem().getDisplayMetrics().heightPixels;
        mImageView.getLayoutParams().height = height*3/5;
        mImageView.requestLayout();
        //creating bitmaps
        avatar = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
        ladder = BitmapFactory.decodeResource(getResources(), R.drawable.ladder);
        snake = BitmapFactory.decodeResource(getResources(), R.drawable.snake);

        //UI elements
        diceButton=(ImageView) findViewById(R.id.diceButton);
        information=(TextView) findViewById(R.id.information);
        mainView=(View) findViewById(R.id.main);

        //setting canvas to the image holder view
        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Boolean mMeasured = false;

                if (!mMeasured) {

                    // Here your view is already laied out and measured for the first time
                    drawing.drawBoard(mImageView,ladder,snake);
                    drawing.showAvatarInTile(userTile, avatar);
                    pLocation.writeToFile(drawing.sqWidth,drawing.sqHeight,boardSize,10);
                    mMeasured = true; // Some optional flag to mark, that we already got the sizes
                }
            }
        });

    }
    void manageUserMovement(){
        if (userTile>=boardSize*boardSize & userGoal>=boardSize*boardSize){
            //celebrate

            Intent finish = new Intent(this,Finish.class);
            startActivityForResult(finish, 1);

        }
        else {
            ((TextView) findViewById(R.id.textView1)).setText("");
            drawing.showAvatarInTile(userTile,avatar);
            if (userTile==userGoal){
                diceButton.setVisibility(View.VISIBLE);
                information.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void getDiceNumber(View view){
        Intent rollDice = new Intent(this,DiceRoll.class);
        startActivityForResult(rollDice, ROLL_Dice_REQUEST);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ROLL_Dice_REQUEST) {
            if (resultCode == RESULT_OK) {
                //prev goal set
                userGoal+=data.getIntExtra("diceNumber",1);
                //show star
                diceButton.setVisibility(View.INVISIBLE);
                String message=String.format ("Goal: %d", userGoal);
                information.setText(message);
                information.setVisibility(View.VISIBLE);


            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Point p=pLocation.toPoint(location);
        Log.e("to point now","p="+p);

        //if tile changes
        int gpstile=drawing.board.pointToTile(p.x+drawing.OFFSET,p.y+drawing.OFFSET);
        Log.d("gtile","gtile"+gpstile);

        if (userTile!=gpstile+1 & gpstile!=-1){
            userTile=gpstile+1;
            manageUserMovement();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Location","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Location","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Location","status");
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
    }
}

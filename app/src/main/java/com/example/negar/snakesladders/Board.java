package com.example.negar.snakesladders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.negar.snakesladders.utility.Drawing;

import com.example.negar.snakesladders.utility.PLocation;

import org.json.JSONException;
import org.json.JSONObject;



public class Board extends AppCompatActivity implements LocationListener {
    Boolean emulating=true;
    private ImageView mImageView;
    private Drawing drawing;
    private Bitmap avatar;
    ImageView diceButton;

    //get the size from indent later
    private int boardSize=5;
    private int userTile=1;
    private int userPrevTile=1;
    private int userGoal=1;


    //communicating with other activities
    static final int ROLL_Dice_REQUEST = 1;  // The request code

    //gps
    double boardLong,boardLat;
    protected LocationManager locationManager;
    protected String latitude,longitude;

    private PLocation pLocation;
    double mpp=0.025;

    final Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setting colors and
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        //location settings
        Intent intent = getIntent();
        String location = intent.getStringExtra("centerLocation");

        try {
            JSONObject locationObj = new JSONObject(location);
            boardLat = locationObj.getDouble("lat");
            boardLong = locationObj.getDouble("long");
            if (emulating) {
                double mpp=0.025;
            }

            pLocation=new PLocation(boardLat,boardLong,12.742*1000000,mpp);
        } catch (JSONException e) {
            Log.e("json", e.toString());
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);


        //board display settings
        int bg = ResourcesCompat.getColor(getResources(), R.color.colorBackground, null);
        int rect = ResourcesCompat.getColor(getResources(), R.color.colorRectangle, null);
        int txt = ResourcesCompat.getColor(getResources(), R.color.colorBoardText, null);
        drawing = new Drawing(bg, rect, txt, boardSize);

        mImageView = (ImageView) findViewById(R.id.myimageview);
        avatar = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
        diceButton=(ImageView) findViewById(R.id.diceButton);

        mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Boolean mMeasured = false;

                if (!mMeasured) {

                    // Here your view is already layed out and measured for the first time
                    drawing.drawBoard(mImageView);
                    drawing.showAvatarInTile(userTile, userPrevTile, avatar);
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
            drawing.showAvatarInTile(userTile, userPrevTile, avatar);
            if (userTile==userGoal){
                diceButton.setVisibility(View.VISIBLE);

            }
        }

    }

    void getDiceNumber(View view){
        Intent rollDice = new Intent(this,DiceRoll.class);
        startActivityForResult(rollDice, ROLL_Dice_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ROLL_Dice_REQUEST) {
            if (resultCode == RESULT_OK) {
                //prev goal set
                userGoal+=data.getIntExtra("diceNumber",1);
                //show star
                diceButton.setVisibility(View.INVISIBLE);


            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("update","Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        Log.e("center","Latitude:" + boardLat + ", Longitude:" + boardLong);

        Point p=pLocation.toPoint(location);
        Log.e("to point now","p="+p);


        //if tile changes
        int gpstile=drawing.board.pointToTile(p.x+drawing.OFFSET,p.y+drawing.OFFSET);
        Log.d("gtile","gtile"+gpstile);

        if (userTile!=gpstile+1 & gpstile!=-1){
            userPrevTile=userTile;
            userTile=gpstile+1;
            manageUserMovement();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

}

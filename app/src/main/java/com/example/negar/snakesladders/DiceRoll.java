package com.example.negar.snakesladders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.negar.snakesladders.utility.PreLollipopSoundPool;
import com.example.negar.snakesladders.utility.ShakeDetector;



public class DiceRoll extends AppCompatActivity {
    ImageView dice_picture;     //reference to dice picture
    TextView textView;
    Random rng=new Random();    //generate random numbers
    private SoundPool dice_sound;       //For dice sound playing
    private int sound_id;               //Used to control sound stream return by SoundPool
    private Handler handler;            //Post message to start roll
    private Timer timer=new Timer();    //Used to implement feedback to user
    private boolean rolling=false;      //Is die rolling?

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_roll);
        InitSound();
        //Get a reference to image widget
        dice_picture = (ImageView) findViewById(R.id.imageView);
        textView=(TextView) findViewById(R.id.information);
        handler=new Handler(callback);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();

        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                if (!rolling) {
                    rolling = true;
                    Roll roll=new Roll();
                    dice_picture.setImageResource(R.drawable.dice3droll);
                    dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);
                    //Pause to allow image to update
                    timer.schedule(roll, 1000);
                    //go to another activity;
                    goBackToCaller(roll.diceNumber);
                }
            }
        });
    }

    void InitSound() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            dice_sound= new SoundPool.Builder().setAudioAttributes(aa).build();

        } else {
            //Running on device earlier than Lollipop
            dice_sound= PreLollipopSoundPool.NewSoundPool();
        }
        //Load the dice sound
        sound_id=dice_sound.load(this,R.raw.shake_dice,1);
    }

    //When pause completed message sent to callback
    class Roll extends TimerTask {
        int diceNumber=rng.nextInt(6)+1;
;
        public void run() {
            handler.sendEmptyMessage(diceNumber);
        }
    }

    //Receives message from timer to start dice roll
    Handler.Callback callback = new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            //Get roll result
            switch(msg.what) {
                case 1:
                    dice_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    dice_picture.setImageResource(R.drawable.two);
                    break;
                case 3:
                    dice_picture.setImageResource(R.drawable.three);
                    break;
                case 4:
                    dice_picture.setImageResource(R.drawable.four);
                    break;
                case 5:
                    dice_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    dice_picture.setImageResource(R.drawable.six);
                    break;
                default:
            }
            rolling=false;  //user can press again

            String message=String.format ("Go %d tiles forwars", msg.what);
            textView.setText(message);

            return true;
        }
    };


    void goBackToCaller(int msg){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("diceNumber", msg);
        setResult(Activity.RESULT_OK, resultIntent);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 2000);
    }

    //Clean up
    protected void onPause() {
        super.onPause();
        dice_sound.pause(sound_id);
        mSensorManager.unregisterListener(mShakeDetector);

    }
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }
}



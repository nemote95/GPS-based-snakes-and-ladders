package com.example.negar.snakesladders.utility;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class ShakeDetector implements SensorEventListener {

    /*
     * The gForce that is necessary to register as shake.
     * Must be greater than 1G (one earth gravity unit).
     * You can install "G-Force", by Blake La Pierre
     * from the Google Play Store and run it to see how
     *  many G's it takes to register a shake
     */
    private static final float SHAKE_THRESHOLD_GRAVITY = 9.81F;
    private static final int SHAKE_SLOP_TIME_MS = 500;

    private OnShakeListener mListener;

    private long now = 0;
    private long timeDiff = 0;
    private long lastUpdate = 0;
    private long lastShake = 0;

    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;
    private float force = 0;

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public interface OnShakeListener {
        public void onShake(int count);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        now = event.timestamp;

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        // if not interesting in shake events
        // just remove the whole if then else block
        if (lastUpdate == 0) {
            lastUpdate = now;
            lastShake = now;
            lastX = x;
            lastY = y;
            lastZ = z;
            Log.d("shake","no motion");
        } else {
            timeDiff = now - lastUpdate;

            if (timeDiff > 0) {

                force = Math.abs(x + y + z - lastX - lastY - lastZ);

                if (Float.compare(force, SHAKE_THRESHOLD_GRAVITY) > 0) {

                    if (now - lastShake >= SHAKE_SLOP_TIME_MS) {
                        // trigger shake event
                        mListener.onShake(10);
                    } else {
                        Log.d("shake","No Motion detected");

                    }
                    lastShake = now;
                }
                lastX = x;
                lastY = y;
                lastZ = z;
                lastUpdate = now;
            } else {
                Log.d("shake","No Motion detected");
            }
        }
    }
}
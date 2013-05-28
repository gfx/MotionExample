package com.example.motionexample;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    // For shake motion detection.
    private SensorManager sensorMgr;
    private float accel; // acceleration apart from gravity
    private float accelCurrent; // current acceleration including gravity
    private float accelLast; // last acceleration including gravity

    private TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorMgr.registerListener(this, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        accel = 0.00f;
        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelLast    = SensorManager.GRAVITY_EARTH;

        textView = (TextView)findViewById(R.id.text);
        textView.setText("");
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorMgr.registerListener(this, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        sensorMgr.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        final float x = se.values[0];
        final float y = se.values[1];
        final float z = se.values[2];
        accelLast = accelCurrent;
        accelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
        final float delta = accelCurrent - accelLast;
        accel = accel * 0.9f + delta; // perform low-cut filter

        if (Math.abs(accel) > 5.0) {
            textView.append("accel: " + accel + "\n");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

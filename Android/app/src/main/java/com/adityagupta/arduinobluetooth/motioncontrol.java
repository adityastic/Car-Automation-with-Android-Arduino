package com.adityagupta.arduinobluetooth;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

public class motioncontrol extends Activity implements SensorEventListener {


    TextView direction1;

    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private long lastUpdate = -1;
    private float x, y, z;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;

    private int loopValue = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);
        direction1 = (TextView) findViewById(R.id.direction);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();

        if (getIntent().hasExtra("loopValue")) {
            loopValue = getIntent().getIntExtra("loopValue", 150);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float[] values = event.values;

            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];


            Log.d("Shakes", "X: " + x + "  Y: " + y + "  Z: " + z);

            long actualTime = System.currentTimeMillis();
            if ((actualTime - lastUpdate) > 100) {
                long diffTime = (actualTime - lastUpdate);
                lastUpdate = actualTime;

                if (Round(x, 4) > 8.0000) {
                    Log.d("sensor", "=====LEFT====");
                    direction1.setText("left");
                    if (BluetoothUtils.getB().isConnected())
                        for (int i = 0; i < loopValue; i++) {
                            BluetoothUtils.getB().send("c");
                        }
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);

                } else if (Round(x, 4) < -8.0000) {
                    Log.d("sensor", "=====RIGHT====");
                    direction1.setText("right");
                    if (BluetoothUtils.getB().isConnected())
                        for (int i = 0; i < loopValue; i++) {
                            BluetoothUtils.getB().send("d");
                        }

                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                } else if (z > 9 && z < 10) {
                    Log.d("sensor", "=====DOWN====");
                    direction1.setText("Forward");

                    if (BluetoothUtils.getB().isConnected())
                        for (int i = 0; i < loopValue; i++) {
                            BluetoothUtils.getB().send("a");
                        }
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                } else if (z > -14 && z < -4) {
                    Log.d("sensor", "=====UP====");
                    direction1.setText("backward");
                    if (BluetoothUtils.getB().isConnected())
                        for (int i = 0; i < loopValue; i++) {
                            BluetoothUtils.getB().send("b");
                        }

                    getWindow().getDecorView().setBackgroundColor(Color.GRAY);
                }

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }

    }

    public static float Round(float Rval, int Rpl) {
        float p = (float) Math.pow(10, Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (float) tmp / p;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

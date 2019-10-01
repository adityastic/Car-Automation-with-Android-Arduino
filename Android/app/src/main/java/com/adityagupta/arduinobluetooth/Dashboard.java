package com.adityagupta.arduinobluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import me.aflak.bluetooth.Bluetooth;

public class Dashboard extends AppCompatActivity implements Bluetooth.CommunicationCallback {

    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        findViewById(R.id.drive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BluetoothUtils.getB().isConnected())
                    startActivity(new Intent(Dashboard.this, MainActivity.class));
                else
                    Toast.makeText(Dashboard.this, "Not Connected", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.selfdrive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BluetoothUtils.getB().isConnected())
                    startActivity(new Intent(Dashboard.this, SelfDrive.class));
                else
                    Toast.makeText(Dashboard.this, "Not Connected", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.voicecontrol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BluetoothUtils.getB().isConnected())
                    startActivity(new Intent(Dashboard.this, voicecontrol.class));
                else
                    Toast.makeText(Dashboard.this, "Not Connected", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.motioncontrol).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BluetoothUtils.getB().isConnected()) {
                    Intent i = new Intent(Dashboard.this, motioncontrol.class);
                    int value = (((EditText) findViewById(R.id.motionEdit)).getText().toString().trim().equals("")) ? 150 : Integer.parseInt(((EditText) findViewById(R.id.motionEdit)).getText().toString().trim());
                    i.putExtra("loopValue", value);
                    startActivity(i);
                } else
                    Toast.makeText(Dashboard.this, "Not Connected", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.obstacle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Switch) findViewById(R.id.obstacleSwitch)).setChecked(
                        !((Switch) findViewById(R.id.obstacleSwitch)).isChecked()
                );
            }
        });

        findViewById(R.id.selfLearning).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BluetoothUtils.getB().isConnected()) {
                    try {
                        final JSONArray arr = new JSONArray(PreferenceManager.getDefaultSharedPreferences(Dashboard.this).getString("trained_data", "[]"));

                        i = 0;
                        new CountDownTimer(arr.length() * 55, 55) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                try {
                                    BluetoothUtils.getB().send(arr.getString(i));
                                    Log.e("Current", arr.getString(i));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                i++;
                            }

                            @Override
                            public void onFinish() {

                            }
                        }.start();
                    } catch (JSONException e) {
                        Toast.makeText(Dashboard.this, "JSON Error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(Dashboard.this, "Not Connected", Toast.LENGTH_SHORT).show();
            }
        });

        ((Switch) findViewById(R.id.obstacleSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (BluetoothUtils.getB().isConnected())
                    BluetoothUtils.getB().send((isChecked) ? "i" : "j");
                else
                    Toast.makeText(Dashboard.this, "Not Connected", Toast.LENGTH_SHORT).show();
            }
        });

        setupTypeFace();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.train) {
                    startActivity(new Intent(Dashboard.this, TrainActivity.class));
                }
                return true;
            }
        });

        BluetoothUtils.setB(new Bluetooth(this));
        BluetoothUtils.getB().enableBluetooth();

        BluetoothUtils.getB().setCommunicationCallback(this);
        BluetoothUtils.getB().connectToDevice(BluetoothUtils.getB().getPairedDevices().get(0));
    }

    private void setupTypeFace() {
        Typeface typefaceBold = TypefaceHelper.get(this, getResources().getString(R.string.sans_bold));
        ((AppCompatTextView) findViewById(R.id.driveTitle)).setTypeface(typefaceBold);
        ((AppCompatTextView) findViewById(R.id.selfDriveTitle)).setTypeface(typefaceBold);
        ((AppCompatTextView) findViewById(R.id.motionTitle)).setTypeface(typefaceBold);
        ((AppCompatTextView) findViewById(R.id.voiceTitle)).setTypeface(typefaceBold);
        ((AppCompatTextView) findViewById(R.id.obstacleTitle)).setTypeface(typefaceBold);
        ((AppCompatTextView) findViewById(R.id.selfLearningTitle)).setTypeface(typefaceBold);
    }

    @Override
    public void onConnect(final BluetoothDevice device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Dashboard.this, "Connected to " + device.getName() + " - " + device.getAddress(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Dashboard.this, "Error: " + "Disconnected!, Connecting again...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessage(final String message) {
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onConnectError(BluetoothDevice device, String message) {

    }
}

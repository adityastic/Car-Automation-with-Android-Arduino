package com.adityagupta.arduinobluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import me.aflak.bluetooth.Bluetooth;

public class TrainActivity extends AppCompatActivity implements Bluetooth.CommunicationCallback {

    TextView t1;
    ImageButton forward, backward, right, left, back_right, back_left;
    Button start, end;

    Boolean isStarted = false;
    JSONArray listSteps;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        t1 = findViewById(R.id.textView1);
        forward = findViewById(R.id.forward);
        backward = findViewById(R.id.backward);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        back_right = findViewById(R.id.back_right);
        back_left = findViewById(R.id.back_left);

        start = findViewById(R.id.start);
        end = findViewById(R.id.end);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listSteps = new JSONArray();
                isStarted = true;
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStarted = false;
                prefs.edit().putString("trained_data", listSteps.toString()).apply();
                Log.e("Data_Train",listSteps.toString());
            }
        });

        forward.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarted) {
                    if (BluetoothUtils.getB().isConnected())
                        BluetoothUtils.getB().send("a");
                    listSteps.put("a");
                }
            }
        }));

        backward.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarted) {
                    if (BluetoothUtils.getB().isConnected())
                        BluetoothUtils.getB().send("b");
                    listSteps.put("b");
                }
            }
        }));

        right.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarted) {
                    if (BluetoothUtils.getB().isConnected())
                        BluetoothUtils.getB().send("c");
                    listSteps.put("c");
                }
            }
        }));

        left.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarted) {
                    if (BluetoothUtils.getB().isConnected())
                        BluetoothUtils.getB().send("d");
                    listSteps.put("d");
                }
            }
        }));

        back_right.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarted) {
                    if (BluetoothUtils.getB().isConnected())
                        BluetoothUtils.getB().send("e");
                    listSteps.put("e");
                }
            }
        }));

        back_left.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarted) {
                    if (BluetoothUtils.getB().isConnected())
                        BluetoothUtils.getB().send("f");
                    listSteps.put("f");
                }
            }
        }));


        BluetoothUtils.getB().setCommunicationCallback(this);
    }

    @Override
    public void onConnect(final BluetoothDevice device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrainActivity.this, "Connected to " + device.getName() + " - " + device.getAddress(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrainActivity.this, "Error: " + "Disconnected!, Connecting again...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("Message", message);
            }
        });
    }

    @Override
    public void onError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrainActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectError(final BluetoothDevice device, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrainActivity.this, "Error in Connection : " + message, Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BluetoothUtils.getB().connectToDevice(device);
                    }
                }, 2000);
            }
        });
    }
}

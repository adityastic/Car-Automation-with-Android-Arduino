package com.adityagupta.arduinobluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import me.aflak.bluetooth.Bluetooth;

public class MainActivity extends AppCompatActivity implements Bluetooth.CommunicationCallback {

    TextView t1;
    ImageButton forward, backward, right, left, back_right, back_left;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t1 = findViewById(R.id.textView1);
        forward = findViewById(R.id.forward);
        backward = findViewById(R.id.backward);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        back_right = findViewById(R.id.back_right);
        back_left = findViewById(R.id.back_left);

        forward.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothUtils.getB().send("a");
            }
        }));

        backward.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothUtils.getB().send("b");
            }
        }));

        right.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothUtils.getB().send("c");
            }
        }));

        left.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothUtils.getB().send("d");
            }
        }));

        back_right.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothUtils.getB().send("e");
            }
        }));

        back_left.setOnTouchListener(new RepeatListener(50, 50, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothUtils.getB().send("f");
            }
        }));

        BluetoothUtils.getB().setCommunicationCallback(this);
    }

    @Override
    public void onConnect(final BluetoothDevice device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Connected to " + device.getName() + " - " + device.getAddress(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Error: " + "Disconnected!, Connecting again...", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectError(final BluetoothDevice device, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Error in Connection : " + message, Toast.LENGTH_SHORT).show();
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

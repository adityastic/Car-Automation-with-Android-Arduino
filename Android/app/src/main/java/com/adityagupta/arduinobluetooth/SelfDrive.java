package com.adityagupta.arduinobluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import me.aflak.bluetooth.Bluetooth;

public class SelfDrive extends AppCompatActivity implements Bluetooth.CommunicationCallback {

    private static final String TAG = "SelfDrive";

    Button rotate;
    TextView anaResult;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_drive);

        anaResult = findViewById(R.id.anaresult);
        rotate = findViewById(R.id.rotate);
        Button sample = findViewById(R.id.sample);

        //Bluetooth Config
        BluetoothUtils.getB().setCommunicationCallback(this);

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BluetoothUtils.getB().isConnected()) {
                    BluetoothUtils.getB().send("g");
                } else
                    Toast.makeText(SelfDrive.this, "Let it Connect", Toast.LENGTH_SHORT).show();
            }
        });

        sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothUtils.getB().send("h");
            }
        });
    }

    @Override
    public void onConnect(final BluetoothDevice device) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SelfDrive.this, "Connected to " + device.getName() + " - " + device.getAddress(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDisconnect(BluetoothDevice device, String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SelfDrive.this, "Error: " + "Disconnected!, Connecting again...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessage(final String message) {
        if (message.contains("finishanalysis")) {
            printAnalysisReport(message.substring(15));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("Message", message);
            }
        });
    }


    private void printAnalysisReport(final String angle) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                anaResult.setText("Found Optimum Path at " + angle + " Degrees");
            }
        });
    }

    @Override
    public void onError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SelfDrive.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectError(final BluetoothDevice device, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SelfDrive.this, "Error in Connection : " + message, Toast.LENGTH_SHORT).show();
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

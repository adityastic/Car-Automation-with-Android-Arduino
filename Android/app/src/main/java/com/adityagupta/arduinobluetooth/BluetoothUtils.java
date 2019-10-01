package com.adityagupta.arduinobluetooth;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothUtils {
    static private Bluetooth b;

    public static Bluetooth getB() {
        return b;
    }

    public static void setB(Bluetooth b) {
        BluetoothUtils.b = b;
    }
}

package com.adityagupta.arduinobluetooth;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ApplicationActivity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/circular_std_book.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}

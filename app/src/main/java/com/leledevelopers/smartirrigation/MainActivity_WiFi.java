package com.leledevelopers.smartirrigation;

import android.os.Bundle;

import com.leledevelopers.smartirrigation.services.SmsServices;

public class MainActivity_WiFi extends SmsServices {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity_wifi);
    }

    /**
     * This method should contain the code to initialize the UI Views
     *
     * @return void
     */
    @Override
    public void initViews() {

    }

    @Override
    public void enableViews() {

    }

    @Override
    public void disableViews() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
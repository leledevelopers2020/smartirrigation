package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.leledevelopers.smartirrigation.services.SmsServices;

public class MainActivity extends SmsServices {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void sendMessage() {

    }

    @Override
    public void receiveMessage() {

    }
}
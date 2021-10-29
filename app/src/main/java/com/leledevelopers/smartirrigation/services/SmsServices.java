package com.leledevelopers.smartirrigation.services;

import androidx.appcompat.app.AppCompatActivity;

public abstract class SmsServices extends AppCompatActivity {

    abstract public void sendMessage();
    abstract public void receiveMessage();

}

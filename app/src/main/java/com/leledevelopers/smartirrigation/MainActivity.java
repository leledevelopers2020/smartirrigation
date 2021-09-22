package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.registration.Screen_1;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;

import java.util.Date;

public class MainActivity extends SmsServices {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    TextView status;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        this.context = getApplicationContext();
        accessPermissions();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Screen_1.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsReceiver.setContext(getApplicationContext());
        smsReceiver.startBroadcastReceiver();
        smsReceiver.setSmsMessageBroadcast(new SmsReceiver.SmsReceiverBroadcast() {
            @Override
            public void onReceiveSms(String phoneNumber, String message) {
                Log.d("SmsReceiver", "Yup got it!! " + phoneNumber + " , " + message);
                status.setText("Screen 2.1\nSender's Number = " + phoneNumber + "\n Message : " + message);
            }

            @Override
            public void checkTime(String time) {

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
    }

    @Override
    public void initViews() {
        status = findViewById(R.id.status);
        button = findViewById(R.id.settings);
    }
}
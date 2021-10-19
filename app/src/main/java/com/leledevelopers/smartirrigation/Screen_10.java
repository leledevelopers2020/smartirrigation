package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

public class Screen_10 extends SmsServices {
    private static final String TAG = Screen_10.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b;
    EditText noLoadCutoffText, fullLoadCutOffText;
    private Button setMotorLoadThreshold;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen10);
        initViews();
    }

    @Override
    public void initViews() {
        noLoadCutoffText = findViewById(R.id.noLoadCutoffText);
        fullLoadCutOffText = findViewById(R.id.fullLoadCutOffText);
        setMotorLoadThreshold = findViewById(R.id.setMotorLoadThreshold);
        status = findViewById(R.id.screen_10_status);
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsReceiver.setContext(getApplicationContext());
        smsReceiver.startBroadcastReceiver();
        smsReceiver.setSmsMessageBroadcast(new SmsReceiver.SmsReceiverBroadcast() {
            @Override
            public void onReceiveSms(String phoneNumber, String message) {
                b = false;
                if (SmsServices.phoneNumber.replaceAll("\\s", "").equals(phoneNumber.replaceAll("\\s", ""))) {
                    checkSMS(message);
                }
            }

            @Override
            public void checkTime(String time) {
                if (b) {
                    status.setText("System Down");
                }
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

    public void checkSMS(String message) {
        switch (message) {
            case SmsUtils.INSMS_12_1: {
                status.setText("MOTOR Load Threshold is Set");
                break;
            }
        }
    }
}
package com.leledevelopers.smartirrigation;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

public class Screen_9 extends SmsServices {
    private static final String TAG = Screen_9.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button setSystemTime, getSystemTime, updatePassword, setMotorloadCutoff, save;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen9);
        initViews();
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    @Override
    public void initViews() {
        spinner = (Spinner) findViewById(R.id.language_spinner);
        setSystemTime = findViewById(R.id.setSystemTime);
        getSystemTime = findViewById(R.id.getSystemTime);
        updatePassword = findViewById(R.id.updatePassword);
        setMotorloadCutoff = findViewById(R.id.setMotorloadCutoff);
        status = findViewById(R.id.screen_9_status);
        save = findViewById(R.id.save);
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
            case SmsUtils.INSMS_10_1: {
                status.setText("System time set to current time");
                break;
            }
            case SmsUtils.INSMS_10_2: {
                status.setText("System time set failed ");
                break;
            }
            case SmsUtils.INSMS_11_1: {
                status.setText("Current Time:" + message);
                break;
            }
        }
    }
}
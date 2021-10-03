package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

public class Screen_7 extends AppCompatActivity {
    private static final String TAG = Screen_7.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b;
    EditText filtrationControlUnitNoDelay_1, filtrationControlUnitNoDelay_2, filtrationControlUnitNoDelay_3;
    EditText filtrationControlUnitOnTime, filtrationControlUnitSeparation;
    private Button enableFiltration7, disableFiltration7;
    private TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen7);
        initViews();
    }

    private void initViews() {
        filtrationControlUnitNoDelay_1 = findViewById(R.id.filtrationControlUnitNoDelay_1);
        filtrationControlUnitNoDelay_2 = findViewById(R.id.filtrationControlUnitNoDelay_2);
        filtrationControlUnitNoDelay_3 = findViewById(R.id.filtrationControlUnitNoDelay_3);
        filtrationControlUnitOnTime = findViewById(R.id.filtrationControlUnitOnTime);
        filtrationControlUnitSeparation = findViewById(R.id.filtrationControlUnitSeparation);
        enableFiltration7 = findViewById(R.id.enableFiltration7);
        disableFiltration7 = findViewById(R.id.disableFiltration7);
        status = findViewById(R.id.screen_7_status);
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
                checkSMS(message);
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
            case SmsUtils.INSMS_8_1: {
                status.setText("Pump Filtration Activated");
                break;
            }
            case SmsUtils.INSMS_9_1: {
                status.setText("Pump Filtration De-Activated");
                break;
            }
        }
    }
}
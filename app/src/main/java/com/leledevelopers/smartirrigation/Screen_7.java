package com.leledevelopers.smartirrigation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.models.FiltrationModel;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

public class Screen_7 extends SmsServices {
    private static final String TAG = Screen_7.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b;
    EditText filtrationControlUnitNoDelay_1, filtrationControlUnitNoDelay_2, filtrationControlUnitNoDelay_3;
    EditText filtrationControlUnitOnTime, filtrationControlUnitSeparation;
    private Button enableFiltration, disableFiltration;
    private TextView status;
    private FiltrationModel model;
    private CURD_Files curd_files = new CURD_FilesImpl();
    private SmsUtils smsUtils = new SmsUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen7);
        initViews();
    }

    @Override
    public void initViews() {
        filtrationControlUnitNoDelay_1 = findViewById(R.id.filtrationControlUnitNoDelay_1);
        filtrationControlUnitNoDelay_2 = findViewById(R.id.filtrationControlUnitNoDelay_2);
        filtrationControlUnitNoDelay_3 = findViewById(R.id.filtrationControlUnitNoDelay_3);
        filtrationControlUnitOnTime = findViewById(R.id.filtrationControlUnitOnTime);
        filtrationControlUnitSeparation = findViewById(R.id.filtrationControlUnitSeparation);
        enableFiltration = findViewById(R.id.enableFiltration7);
        disableFiltration = findViewById(R.id.disableFiltration7);
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
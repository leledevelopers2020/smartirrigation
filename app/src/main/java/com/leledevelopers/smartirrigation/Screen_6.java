package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

public class Screen_6 extends AppCompatActivity {
    private static final String TAG = Screen_6.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b;
    private Spinner spinner;
    EditText wetPeriod,injectPeriod,noOfIterations;
    private Button enableFieldFertigation,disableFieldFertigation;
    private ArrayAdapter<CharSequence> adapter;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen6);
        initViews();
        adapter= ArrayAdapter.createFromResource(getApplicationContext(),R.array.selctFieldNoArray,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private void initViews()
    {
        spinner=(Spinner) findViewById(R.id.fieldNoSpinner6);
        wetPeriod=findViewById(R.id.wetPeriod);
        injectPeriod=findViewById(R.id.injectPeriod);
        noOfIterations=findViewById(R.id.noOfIterations);
        enableFieldFertigation=findViewById(R.id.enableFieldFertigation6);
        disableFieldFertigation=findViewById(R.id.disableFieldFertigation6);
        status=findViewById(R.id.screen_6_status);
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
            case SmsUtils.INSMS_6_1: {
                status.setText("Fertigation Enabled");
                break;
            }
            case SmsUtils.INSMS_6_2: {
                status.setText("Wrong Fertigation time send, fertigation is not enabled");
                break;
            }
            case SmsUtils.INSMS_7_1: {
                status.setText("Fertigation Disabled");
                break;
            }
        }
    }
}
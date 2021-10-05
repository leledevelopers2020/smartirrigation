package com.leledevelopers.smartirrigation;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.util.Calendar;

public class Screen_5 extends AppCompatActivity {
    private static final String TAG = Screen_5.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    ArrayAdapter<CharSequence> adapter;
    private Spinner spinner;
    private int hour, min;
    EditText valveOnPeriod, valveOffPeriod, soilDryness, soilWetness, motorOnTime, priority, cycles, wetPeriod;
    TextView status;
    private Button enableFertigation, disableFertigation;
    private Boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen5);
        initViews();
        // spinner of field no
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // motor time
        motorOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Screen_5.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour = hourOfDay;
                                min = minute;
                                Calendar cal = Calendar.getInstance();
                                cal.set(0, 0, 0, hour, min);
                                motorOnTime.setText(DateFormat.format("hh:mm aa", cal));
                            }
                        }, 12, 0, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour, min);
                timePickerDialog.show();
            }
        });

        //enable fertigation
        enableFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //disable fertigation
        disableFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void initViews() {
        spinner = (Spinner) findViewById(R.id.fieldNoSpinner5);
        valveOnPeriod = findViewById(R.id.valveOnPeriod);
        valveOffPeriod = findViewById(R.id.valveOffPeriod);
        soilDryness = findViewById(R.id.soilDryness);
        soilWetness = findViewById(R.id.soilWetness);
        motorOnTime = findViewById(R.id.motorOnTime);
        priority = findViewById(R.id.priority);
        cycles = findViewById(R.id.priority);
        wetPeriod = findViewById(R.id.wetPeriod);
        enableFertigation = findViewById(R.id.enableFieldFertigation5);
        disableFertigation = findViewById(R.id.disableFertigation5);
        status = findViewById(R.id.screen_5_status);
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
            case SmsUtils.INSMS_4_1: {
                status.setText(SmsUtils.INSMS_4_1);
                break;
            }
            case SmsUtils.INSMS_5_1: {
                status.setText("Valve configuration kept on Hold");
                break;
            }
        }
    }
}
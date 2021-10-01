package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Calendar;

public class Screen_5 extends AppCompatActivity {
    ArrayAdapter<CharSequence> adapter;
    private Spinner spinner;
    private int hour,min;
    EditText valveOnPeriod,valveOffPeriod,soilDryness,soilWetness,motorOnTime,priority,cycles,wetPeriod;
    TextView screenStatus5;
    private Button enableFertigation,disableFertigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen5);

        // spinner of field no
        adapter= ArrayAdapter.createFromResource(getApplicationContext(),R.array.selctFieldNoArray,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // motor time
        motorOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(Screen_5.this,
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
                },12,0,false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.updateTime(hour,min);
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
    private void initViews()
    {
        spinner=(Spinner) findViewById(R.id.fieldNoSpinner5);
        valveOnPeriod=findViewById(R.id.valveOnPeriod);
        valveOffPeriod=findViewById(R.id.valveOffPeriod);
        soilDryness=findViewById(R.id.soilDryness);
        soilWetness=findViewById(R.id.soilWetness);
        motorOnTime=findViewById(R.id.motorOnTime);
        priority=findViewById(R.id.priority);
        cycles=findViewById(R.id.priority);
        wetPeriod=findViewById(R.id.wetPeriod);
        enableFertigation=findViewById(R.id.enableFieldFertigation5);
        disableFertigation=findViewById(R.id.disableFertigation5);
        screenStatus5=findViewById(R.id.screen_5_status);
    }
}
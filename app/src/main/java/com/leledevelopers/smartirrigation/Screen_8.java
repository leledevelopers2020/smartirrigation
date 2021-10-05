package com.leledevelopers.smartirrigation;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.leledevelopers.smartirrigation.services.CalendarService;

public class Screen_8 extends AppCompatActivity {
    Button fromDate, toDate;
    ArrayAdapter<CharSequence> adapter;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen8);
        initViews();
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarService calendarService = new CalendarService(getApplicationContext());
                fromDate.setText(calendarService.dateCalculator());
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarService calendarService = new CalendarService(getApplicationContext());
                toDate.setText(calendarService.dateCalculator());
            }
        });
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
     /*   spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }


    private void initViews() {
        fromDate = (Button) findViewById(R.id.fromDate);
        toDate = (Button) findViewById(R.id.toDate);
        spinner = (Spinner) findViewById(R.id.fieldNoSpinner8);
    }
}
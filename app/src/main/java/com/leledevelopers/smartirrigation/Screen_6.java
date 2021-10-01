package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Screen_6 extends AppCompatActivity {

    private Spinner spinner;
    EditText wetPeriod,injectPeriod,noOfIterations;
    private Button enableFieldFertigation,disableFieldFertigation;
    private ArrayAdapter<CharSequence> adapter;
    private TextView status7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen6);
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
        status7=findViewById(R.id.screen_6_status);

    }
}
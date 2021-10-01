package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Screen_9 extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button setSystemTime,getSystemTime,updatePassword,setMotorloadCutoff,save;
    private TextView status9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen9);
        adapter= ArrayAdapter.createFromResource(getApplicationContext(),R.array.selctFieldNoArray,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private void initViews()
    {
        spinner=(Spinner) findViewById(R.id.language_spinner);
        setSystemTime=findViewById(R.id.setSystemTime);
        getSystemTime=findViewById(R.id.getSystemTime);
        updatePassword=findViewById(R.id.updatePassword);
        setMotorloadCutoff=findViewById(R.id.setMotorloadCutoff);
        status9=findViewById(R.id.screen_9_status);
        save=findViewById(R.id.save);
    }
}
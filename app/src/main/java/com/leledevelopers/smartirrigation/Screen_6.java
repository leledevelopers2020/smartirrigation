package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

public class Screen_6 extends AppCompatActivity {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen6);
    }
    private void initViews()
    {
        spinner=(Spinner) findViewById(R.id.fieldNoSpinner6);
    }
}
package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Screen_10 extends AppCompatActivity {

    EditText noLoadCutoffText,fullLoadCutOffText;
    private Button setMotorLoadThreshold;
    private TextView status10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen10);
    }

    private void initViews() {
        noLoadCutoffText=findViewById(R.id.noLoadCutoffText);
        fullLoadCutOffText=findViewById(R.id.fullLoadCutOffText);
        setMotorLoadThreshold=findViewById(R.id.setMotorLoadThreshold);
        status10=findViewById(R.id.screen_10_status);
    }

}
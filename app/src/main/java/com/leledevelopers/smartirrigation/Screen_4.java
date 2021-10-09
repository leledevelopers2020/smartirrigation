package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.services.SmsServices;

public class Screen_4 extends SmsServices {

    private static final String TAG = Screen_4.class.getSimpleName();
    TextView status;
    Button settings, configureFieldIrrigation, configureFieldFertigation, configurePumpFiltration, printReportFieldStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen4);
        initViews();
        this.context = getApplicationContext();
        accessPermissions();
        configureFieldIrrigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Navigating to Screen 5");
                startActivity(new Intent(Screen_4.this, Screen_5.class));
            }
        });
        configureFieldFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Navigating to Screen 6");
                startActivity(new Intent(Screen_4.this, Screen_6.class));
            }
        });
        configurePumpFiltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Navigating to Screen 7");
                startActivity(new Intent(Screen_4.this, Screen_7.class));
            }
        });
        printReportFieldStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Navigating to Screen 8");
                startActivity(new Intent(Screen_4.this, Screen_8.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Navigating to Screen 9");
                startActivity(new Intent(Screen_4.this, Screen_9.class));
            }
        });
    }

    @Override
    public void initViews() {
        status = findViewById(R.id.screen_4_status);
        configureFieldIrrigation = findViewById(R.id.screen_4_button1);
        configureFieldFertigation = findViewById(R.id.screen_4_button2);
        configurePumpFiltration = findViewById(R.id.screen_4_button3);
        printReportFieldStatus = findViewById(R.id.screen_4_button4);
        settings = findViewById(R.id.settings);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
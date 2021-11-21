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
                status.setText("Configure Field Irrigation");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_4.this, Screen_5.class));
                        finish();
                    }
                },1000);
            }
        });
        configureFieldFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Configure field fertigation");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_4.this, Screen_6.class));
                        finish();
                    }
                },1000);

            }
        });
        configurePumpFiltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Configure Pump Filtration");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_4.this, Screen_7.class));
                        finish();
                    }
                },1000);

            }
        });
        printReportFieldStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Print report for field status");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_4.this, Screen_8.class));
                        finish();
                    }
                },1000);

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_4.this, Screen_9.class));
                finish();
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
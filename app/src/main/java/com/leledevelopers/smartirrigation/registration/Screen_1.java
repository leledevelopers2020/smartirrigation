package com.leledevelopers.smartirrigation.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.R;
import com.leledevelopers.smartirrigation.services.SmsServices;

public class Screen_1 extends SmsServices {

    private static final String TAG = Screen_1.class.getSimpleName();
    private Button gsmButton, wifiButton;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);
        initViews();
        this.context = getApplicationContext();

            accessPermissions();

        gsmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("GSM Mode Selected");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_1.this, Screen_2_1.class));
                    }
                }, 1000);

            }
        });
        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Wifi Mode Selected");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_1.this, screen_2_2.class));
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void initViews() {
        gsmButton = findViewById(R.id.screen_1_gsm_button);
        wifiButton = findViewById(R.id.screen_1_wifi_button);
        status = findViewById(R.id.screen_1_status);
    }

    @Override
    public void enableViews() {

    }

    @Override
    public void disableViews() {

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
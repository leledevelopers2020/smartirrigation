package com.leledevelopers.smartirrigation.registration;

import android.content.Intent;
import android.os.Bundle;

import com.leledevelopers.smartirrigation.R;
import com.leledevelopers.smartirrigation.services.SmsServices;

public class screen_2_2 extends SmsServices {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen22);

    }

    /**
     * This method should contain the code to initialize the UI Views
     *
     * @return void
     */
    @Override
    public void initViews() {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(screen_2_2.this,Screen_1.class));
        finish();
    }
}
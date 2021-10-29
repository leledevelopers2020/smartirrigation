package com.leledevelopers.smartirrigation.starter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.leledevelopers.smartirrigation.R;
import com.leledevelopers.smartirrigation.registration.Screen_1;
import com.leledevelopers.smartirrigation.MainActivity_GSM;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class splashScreen extends SmsServices {
    boolean registered; ///this value is based on database if available it is set to true or else set to false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        File file = new File(splashScreen.this.getExternalFilesDir(null) + ProjectUtils.FILE_PATH);
        if (file.exists()) {
            this.context = getApplicationContext();
            readUserFile();
            registered = true;
        } else {
            registered = false;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (registered) {
                    Intent intent = new Intent(splashScreen.this,
                            MainActivity_GSM.class);                     // if registered with GSM it goes to screen_3_1 or else if with wi-fi goes to screen_3_2
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(splashScreen.this,
                            Screen_1.class);                                //if not registered goes to registration page
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);

    }

    /**
     * This method should contain the code to initialize the UI Views
     *
     * @return void
     */
    @Override
    public void initViews() {
        //nothing
    }

    private void readUserFile() {
        File file = new File(splashScreen.this.getExternalFilesDir(null) + ProjectUtils.FILE_PATH);
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine())!= null){
                    text.append(line);
                }
                String[] s = text.toString().split("[#]");
                SmsServices.phoneNumber = s[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
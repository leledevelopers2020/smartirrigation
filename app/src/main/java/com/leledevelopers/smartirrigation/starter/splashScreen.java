package com.leledevelopers.smartirrigation.starter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.leledevelopers.smartirrigation.R;
import com.leledevelopers.smartirrigation.registration.Screen_1;
import com.leledevelopers.smartirrigation.registration.Screen_3_1;

import java.io.File;

public class splashScreen extends AppCompatActivity {


    boolean registered; ///this value is based on database if available it is set to true or else set to false;
    private final String fileName="details.txt";
    private final String filePath="MyFileDir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        File file=new File( splashScreen.this.getExternalFilesDir(null)+"/MyFileDir/details.txt");
        if(file.exists())
        {
            registered=true;
        }
        else {
            registered=false;
        }
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(registered)
                {
                    Intent intent=new Intent(splashScreen.this,
                            Screen_3_1.class);                     // if registered with GSM it goes to screen_3_1 or else if with wi-fi goes to screen_3_2
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent=new Intent(splashScreen.this,
                            Screen_1.class);                                //if not registered goes to registration page
                    startActivity(intent);
                    finish();
                }
            }
        },3000);

    }
}
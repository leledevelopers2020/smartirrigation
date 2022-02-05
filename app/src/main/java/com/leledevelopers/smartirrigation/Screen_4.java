package com.leledevelopers.smartirrigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.models.Message;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Screen_4 extends SmsServices {

    private static final String TAG = Screen_4.class.getSimpleName();
    private  Handler handler = new Handler();
    TextView status;
    SmsReceiver smsReceiver=new SmsReceiver();
    ImageView rtcBattery;
    Button settings, configureFieldIrrigation, configureFieldFertigation, configurePumpFiltration, printReportFieldStatus;
    List<Message> messages = new ArrayList<Message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen4);
        initViews();
        this.context = getApplicationContext();
        accessPermissions();
      //  smsReceiver.cancelTimer();
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
                }, 1000);
            }
        });
        configureFieldFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Configure Field Fertigation");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_4.this, Screen_6.class));
                        finish();
                    }
                }, 1000);

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
                }, 1000);

            }
        });
        printReportFieldStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Print report For Field Status");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_4.this, Screen_8.class));
                        finish();
                    }
                }, 1000);

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("Settings");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_4.this, Screen_9.class));
                        finish();
                    }
                }, 1000);

            }
        });




    }

    private void filterMessages() {
        int i = 0;
        boolean isRTC = false;
        for (Message message :
                messages) {
            if (message.getAction().contains(SmsUtils.RTC_BATTERY_FULL_STATUS)) {
                rtcBattery.setImageResource(0);
                rtcBattery.setImageResource(R.drawable.full_battery_green);
                isRTC = true;
            }
            if (message.getAction().contains(SmsUtils.RTC_BATTERY_LOW_STATUS)) {
                rtcBattery.setImageResource(0);
                rtcBattery.setImageResource(R.drawable.empty_battery_red);
                isRTC = true;
            }
        }
        if (!isRTC) {
            rtcBattery.setImageResource(R.drawable.empty_battery);
        }
    }

    @Override
    public void initViews() {
        status = findViewById(R.id.screen_4_status);
        configureFieldIrrigation = findViewById(R.id.screen_4_button1);
        configureFieldFertigation = findViewById(R.id.screen_4_button2);
        configurePumpFiltration = findViewById(R.id.screen_4_button3);
        printReportFieldStatus = findViewById(R.id.screen_4_button4);
        settings = findViewById(R.id.settings);
        rtcBattery = findViewById(R.id.batterystatus);
    }

    @Override
    public void enableViews() {

    }

    @Override
    public void disableViews() {

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder exit = new AlertDialog.Builder(this);
        exit.setMessage("Are you sure do you want to exit")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = exit.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            readAllMessages();
            messages = getSMS();
            filterMessages();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
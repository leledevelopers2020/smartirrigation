package com.leledevelopers.smartirrigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leledevelopers.smartirrigation.registration.Screen_1;
import com.leledevelopers.smartirrigation.registration.Screen_2_1;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity_GSM extends SmsServices {
    private static final String TAG = MainActivity_GSM.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private TextView smsLabel, status;
    private Button connect, resetConnection;
    private Boolean b,systemDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(Build.VERSION.SDK_INT);
        System.out.println(SmsServices.phoneNumber.replaceAll("\\s", ""));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainactivity_gsm);
        initViews();
        readUserFile();
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!systemDown){
                    smsReceiver.waitFor_1_Minute();
                    b = true;
                    sendMessage(SmsServices.phoneNumber, SmsUtils.OutSMS_2);
                    status.setText(SmsUtils.OutSMS_2 + " delivery");
                }
              //  startActivity(new Intent(MainActivity_GSM.this, Screen_4.class));
            }
        });

        resetConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog();
            }
        });
    }

    private void openDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to reset the data");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                         new File(MainActivity_GSM.this.getExternalFilesDir(null) + ProjectUtils.FILE_PATH).delete();
                         status.setText("Reset Successful");
                         handler.postDelayed(new Runnable() {
                             @Override
                             public void run() {
                                 startActivity(new Intent(MainActivity_GSM.this, Screen_1.class));
                             }
                         },1000);
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void initViews() {
        smsLabel = findViewById(R.id.screen_3_1_textview_1);
        connect = findViewById(R.id.screen_3_1_button_1);
        resetConnection = findViewById(R.id.screen_3_1_button_2);
        status = findViewById(R.id.screen_3_1_status);
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsReceiver.setContext(getApplicationContext());
        smsReceiver.startBroadcastReceiver();
        smsReceiver.setSmsMessageBroadcast(new SmsReceiver.SmsReceiverBroadcast() {
            @Override
            public void onReceiveSms(String phoneNumber, String message) {
                b = false;
                if (SmsServices.phoneNumber.replaceAll("\\s", "").equals(phoneNumber.replaceAll("\\s", "")) && !systemDown) {
                    checkSMS(message);
                } else if(phoneNumber.contains(SmsServices.phoneNumber.replaceAll("\\s",""))  && !systemDown) {
                   // System.out.println("Screen 2.1\nSender's Number = " + phoneNumber + "\n Message : " + message);
                    checkSMS(message);
                }
            }

            @Override
            public void checkTime(String time) {
                if (b) {
                    systemDown = true;
                    smsReceiver.unRegisterBroadCasts();
                    status.setText("System Down");
                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();

    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
    }

    public void checkSMS(String message) {
        switch (message) {
            case SmsUtils.INSMS_2_1: {
                status.setText("Connection Successful");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       Intent intent=new Intent(MainActivity_GSM.this, Screen_4.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                },1000);
                break;
            }
            case SmsUtils.INSMS_2_2: {
                status.setText("Admin Changed, please reauthenticate device");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity_GSM.this, Screen_2_1.class));
                        finish();
                    }
                },1000);
                break;
            }
        }
    }

    private void readUserFile() {
        File file = new File(MainActivity_GSM.this.getExternalFilesDir(null) + ProjectUtils.FILE_PATH);
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line);
                }
                String[] s = text.toString().split("[#]");
                SmsServices.phoneNumber = s[0];
                smsLabel.setText(s[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

public class Screen_10 extends SmsServices {
    private static final String TAG = Screen_10.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private SmsUtils smsUtils=new SmsUtils();
    private Boolean b,systemDown = false;
    EditText noLoadCutoffText, fullLoadCutOffText;
    private Button setMotorLoadThreshold,back_10;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen10);
        initViews();
        setMotorLoadThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput(noLoadCutoffText.getText().toString(),fullLoadCutOffText.getText().toString()) && !systemDown)
                {
                    String smsData=smsUtils.OutSMS_12(noLoadCutoffText.getText().toString(),
                            fullLoadCutOffText.getText().toString());
                    sendMessage(SmsServices.phoneNumber,smsData);
                    smsReceiver.waitFor_1_Minute();
                    b = true;
                    status.setText("Message Delivered");
                }
            }
        });
        back_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_10.this,Screen_9.class));
                finish();
            }
        });
    }

    private boolean validateInput(String noLoadCutoffTextlocal, String fullLoadCutOffTextlocal) {
        Boolean validate=true;
        if(noLoadCutoffTextlocal.length()==0)
        {
            noLoadCutoffText.requestFocus();
            noLoadCutoffText.getText().clear();
            noLoadCutoffText.setError("Please enter the data");
            return false;
        }
        if(fullLoadCutOffTextlocal.length()==0)
        {
            fullLoadCutOffText.requestFocus();
            fullLoadCutOffText.getText().clear();
            fullLoadCutOffText.setError("Please enter the data");
        }
        return validate;
    }

    @Override
    public void initViews() {
        noLoadCutoffText = findViewById(R.id.noLoadCutoffText);
        fullLoadCutOffText = findViewById(R.id.fullLoadCutOffText);
        setMotorLoadThreshold = findViewById(R.id.setMotorLoadThreshold);
        status = findViewById(R.id.screen_10_status);
        back_10=findViewById(R.id.back_10);
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
            case SmsUtils.INSMS_12_1: {
                status.setText("MOTOR Load Threshold is Set");
                break;
            }
        }
    }

    
}
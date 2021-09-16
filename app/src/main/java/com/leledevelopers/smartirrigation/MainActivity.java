package com.leledevelopers.smartirrigation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.leledevelopers.smartirrigation.registration.Screen_1;
import com.leledevelopers.smartirrigation.services.SmsServices;

public class MainActivity extends SmsServices {
    public static final String EXTRA_SMS_SENDER = "extra_sms_sender";
    public static final String EXTRA_SMS_MESSAGE = "extra_sms_message";
    private static final int SMS_REQUEST_CODE = 101;
    private static final String TAG = MainActivity.class.getSimpleName();
    TextView textView;
    Button button;
    public String senderNumber;
    public String message;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    Object[] pdusObj = (Object[]) bundle.get("pdus");

                    if (pdusObj != null) {
                        for (Object aPdusObj : pdusObj) {
                            SmsMessage currentMessage = getIncomingMessage(aPdusObj, bundle);
                            senderNumber = currentMessage.getDisplayOriginatingAddress();
                            message = currentMessage.getDisplayMessageBody();
                            Log.d(TAG, "senderNUm = " + senderNumber + " message = " + message);
                            Toast.makeText(MainActivity.this,"senderNUm = " + senderNumber + " message = " + message,Toast.LENGTH_LONG).show();
                            textView.setText("senderNUm = " + senderNumber + "\nmessage = " + message);
                        }
                    } else {
                        Log.d(TAG, "onReceive is null");
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, "Exception " + e);
            }
            Log.d(TAG, "unregisterReceiver");
            // getApplicationContext().unregisterReceiver(this);
        }
    };
    public SmsMessage getIncomingMessage(Object object, Bundle bundle) {
        SmsMessage smsMessage;
        if (Build.VERSION.SDK_INT >= 23) {
            String format = bundle.getString("format");
            smsMessage = SmsMessage.createFromPdu((byte[]) object, format);
        } else {
            smsMessage = SmsMessage.createFromPdu((byte[]) object);
        }
        return smsMessage;
    }

    private void registerBroadCasts() {
        IntentFilter intentConnection = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        getApplicationContext().registerReceiver(broadcastReceiver, intentConnection);
        Log.d(TAG, "registerReceiver");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.status);
        button = findViewById(R.id.settings);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS}, SMS_REQUEST_CODE);
        }

        //registerBroadCasts();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Screen_1.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadCasts();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (broadcastReceiver != null)
                Log.d(TAG, "unregisterReceiver");
            unregisterReceiver(broadcastReceiver);

        } catch (Exception e) {
        }
    }

    @Override
    public void sendMessage() {
        Log.d(TAG,"sendMessage");
    }

    @Override
    public void receiveMessage() {
        Log.d(TAG,"receiveMessage");
    }




}
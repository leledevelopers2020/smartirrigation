package com.leledevelopers.smartirrigation.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.leledevelopers.smartirrigation.MainActivity;
import com.leledevelopers.smartirrigation.R;

public class Screen_1 extends AppCompatActivity {

    private static final String TAG = "Main" ;
    private TextView textView;
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
                            Toast.makeText(Screen_1.this,"senderNUm = " + senderNumber + " message = " + message,Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_screen1);
        textView = findViewById(R.id.screen_1_1_status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadCasts();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Log.d(TAG, "unregisterReceiver");
        try {
            if (broadcastReceiver != null)
                Log.d(TAG, "unregisterReceiver");
            unregisterReceiver(broadcastReceiver);

        } catch (Exception e) {
        }
    }

}
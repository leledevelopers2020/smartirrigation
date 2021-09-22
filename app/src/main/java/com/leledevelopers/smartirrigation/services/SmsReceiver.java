package com.leledevelopers.smartirrigation.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Narsing Rao.k
 * This class contains code for receiving sms
 */
public class SmsReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    private Context context;
    private BroadcastReceiver broadcastReceiver;
    private SmsReceiverBroadcast smsReceiverBroadcast;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");

    public SmsReceiver() {
        this.smsReceiverBroadcast = null;
    }

    public void setSmsMessageBroadcast(SmsReceiverBroadcast smsReceiverBroadcast) {
        this.smsReceiverBroadcast = smsReceiverBroadcast;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * This method initiates broadcastReceiver, gets the sms and triggers onReceiveSms method in activities
     */
    public void startBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                try {
                    if (bundle != null) {
                        Object[] pdusObj = (Object[]) bundle.get("pdus");
                        if (pdusObj != null) {
                            for (Object aPdusObj : pdusObj) {
                                SmsMessage currentMessage = getIncomingMessage(aPdusObj, bundle);
                                String senderNumber = currentMessage.getDisplayOriginatingAddress();
                                String message = currentMessage.getDisplayMessageBody();
                                Log.d(TAG, "senderNUm = " + senderNumber + " message = " + message);
                                Toast.makeText(context, "senderNUm = " + senderNumber + " message = " + message, Toast.LENGTH_LONG).show();
                                smsReceiverBroadcast.onReceiveSms(senderNumber, message);
                            }
                        } else {
                            Log.d(TAG, "onReceive is null");
                        }
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Exception " + e);
                }
            }
        };
    }


    private SmsMessage getIncomingMessage(Object object, Bundle bundle) {
        SmsMessage smsMessage;
        if (Build.VERSION.SDK_INT >= 23) {
            String format = bundle.getString("format");
            smsMessage = SmsMessage.createFromPdu((byte[]) object, format);
        } else {
            smsMessage = SmsMessage.createFromPdu((byte[]) object);
        }
        return smsMessage;
    }

    /**
     * This method registers the broadcastReceiver object
     */
    public void registerBroadCasts() {
        IntentFilter intentConnection = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        context.registerReceiver(broadcastReceiver, intentConnection);
    }

    /**
     * This method unregisters the broadcastReceiver object
     */
    public void unRegisterBroadCasts() {
        try {
            if (broadcastReceiver != null) {
                Log.d(TAG, "unregisterReceiver");
                context.unregisterReceiver(broadcastReceiver);
            }
        } catch (Exception e) {
        }
    }

    public void waitFor_1_Minute(){
        Date date = new Date();
        long d1 = date.getTime();
        Log.d("SmsReceiver", "old time "+d1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                long d2 = new Date().getTime();
                Log.d("SmsReceiver", "new time "+d2);
                long l = d2-d1;
                smsReceiverBroadcast.checkTime((((l / (1000 * 60)) % 60))+"");
            }
        }, 60 * 1000);
    }

    /**
     * @author Narsing Rao.K
     * This interface is used to trigger the sms in activities.
     */
    public interface SmsReceiverBroadcast {
        public void onReceiveSms(String phoneNumber, String message);
        public void checkTime(String time);
    }
}

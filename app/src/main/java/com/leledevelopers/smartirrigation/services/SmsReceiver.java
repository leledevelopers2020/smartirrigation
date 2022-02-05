package com.leledevelopers.smartirrigation.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Narsing Rao.k
 * This class contains code for receiving sms
 */
public class SmsReceiver {
    private  Timer timer,timer3;
    public double randomVal=0;
    HandlerThread handlerThread = new HandlerThread("MyHandlerThread");
    int count=0;
    private static final String TAG = SmsReceiver.class.getSimpleName();
    private Context context;
    private BroadcastReceiver broadcastReceiver;
    private static SmsReceiverBroadcast smsReceiverBroadcast;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
    private Runnable ro;

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
                                smsReceiverBroadcast.onReceiveSms(senderNumber, message);
                            }
                        } else {
                        }
                    }
                } catch (Exception e) {
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

                context.unregisterReceiver(broadcastReceiver);
            }
        } catch (Exception e) {
        }
    }

    public void waitFor_1_Minute(double random,SmsReceiver services) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    smsReceiverBroadcast.checkTime(random);
                    System.out.println("random value = "+random+ " , services "+services.toString());
                }
            }, 60 * 2000);

    /*    timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(count!=0)
                smsReceiverBroadcast. checkTime( randomVal);
            }
        },0,30*1000);
        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {

                    count++;
            }
        },0,2*1000);

        {
             timer3 = new Timer();
            timer3.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {

                        timer2.cancel();
                        System.out.println("count value=" + count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1000);
        }*/
    }
    public  void  cancelTimer()
    {

        count=0;
        try {

            timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            timer3.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 /*   @RequiresApi(api = Build.VERSION_CODES.Q)
    public  void removeCallBacks()
    {
        Log.d("Tag","all the call backs removed");
    }*/

    /**
     * @author Narsing Rao.K
     * This interface is used to trigger the sms in activities.
     */
    public interface SmsReceiverBroadcast {
        public void onReceiveSms(String phoneNumber, String message);

        public void checkTime(double randomValue);
    }
}
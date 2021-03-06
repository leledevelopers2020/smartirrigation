package com.leledevelopers.smartirrigation.services;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.telephony.SmsManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Base64;

public class SmsSender extends AppCompatActivity {
    private SmsSenderBroadcast smsSenderBroadcast;
    private String SENT = "SMS_SENT";
    private String DELIVERED = "SMS_DELIVERED";
    private String testing;
    public SmsSender() {
        this.smsSenderBroadcast = null;
    }

    public void setSmsSenderBroadcast(SmsSenderBroadcast smsSenderBroadcast) {
        this.smsSenderBroadcast = smsSenderBroadcast;
    }

    public void sendMessage(String phoneNumber, String message, Context context, /*TextView view,*/ String screen_Specific_Sms) {
         PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

        /*---when the SMS has been sent---*/
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                testing="SENT";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //view.setText(screen_Specific_Sms + "sent");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // view.setText("Generic failure");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // view.setText("No service");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // view.setText("Null PDU");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        //  view.setText("Radio off, Failed to send SMS");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                    case SmsManager.RESULT_MODEM_ERROR:
                        //view.setText("Failed to Send SMS, Please check your SMS plan");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                    case SmsManager.RESULT_RIL_INVALID_STATE:
                        //view.setText("Failed to Send SMS, Internal Error");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;

                }
            }
        }, new IntentFilter(SENT));

        /*---when the SMS has been delivered---*/
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                testing="DELIVERED";
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //   view.setText(screen_Specific_Sms + "Delivered");
                        smsSenderBroadcast.onReceiveStatus(true);
                        break;
                    case Activity.RESULT_CANCELED:
                        //    view.setText(screen_Specific_Sms + "not delivered");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));


        if (!phoneNumber.equals("") || message != null || !message.equals("")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, encodingMessage(message), sentPI, deliveredPI);
            }

        }

    }

    public interface SmsSenderBroadcast {
        public void onReceiveStatus(boolean smsDeliveredStatus);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String encodingMessage(String actualMessage)
    {
        String encodedMessage
                = Base64.getEncoder()
                .encodeToString(actualMessage.getBytes());
        return encodedMessage;
    }
}

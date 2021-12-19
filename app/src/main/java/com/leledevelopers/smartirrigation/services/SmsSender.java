package com.leledevelopers.smartirrigation.services;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class SmsSender extends AppCompatActivity {
    private SmsSenderBroadcast smsSenderBroadcast;
    private String SENT = "SMS_SENT";
    private String DELIVERED = "SMS_DELIVERED";

    public SmsSender() {
        this.smsSenderBroadcast = null;
    }

    public void setSmsSenderBroadcast(SmsSenderBroadcast smsSenderBroadcast) {
        this.smsSenderBroadcast = smsSenderBroadcast;
    }

    public void sendMessage(String phoneNumber, String message, Context context, TextView view) {
        System.out.println("--> in " + " " + phoneNumber + " " +message);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

        /*---when the SMS has been sent---*/
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                System.out.println("code-----> " + getResultCode());
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        view.setText("SMS sent");
                        //smsSenderBroadcast.onReceiveStatus(true);
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        view.setText("Generic failure");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service",
                                Toast.LENGTH_SHORT).show();
                        view.setText("No service");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        view.setText("Null PDU");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        view.setText("Radio off");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                    case SmsManager.RESULT_MODEM_ERROR:
                        Toast.makeText(context, "Failed to Send SMS, Please check your SMS plan",
                                Toast.LENGTH_SHORT).show();
                        view.setText("Failed to Send SMS, Please check your SMS plan");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                    case SmsManager.RESULT_RIL_INVALID_STATE:
                        Toast.makeText(context, "Internal Error",
                                Toast.LENGTH_SHORT).show();
                        view.setText("Failed to Send SMS, Internal Error");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;

                }
            }
        }, new IntentFilter(SENT));

        /*---when the SMS has been delivered---*/
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                System.out.println("code---> " + getResultCode());
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        view.setText("SMS Delivered");
                        smsSenderBroadcast.onReceiveStatus(true);
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        view.setText("SMS not delivered");
                        smsSenderBroadcast.onReceiveStatus(false);
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));


        if (!phoneNumber.equals("") || message != null || !message.equals("")) {
            System.out.println("hee");
           /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                SubscriptionManager sub = SubscriptionManager.from(context);
                List<SubscriptionInfo> subscriptionInfos = getServiceProviders(context);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                List<SubscriptionInfo> subscriptionInfos1 = sub.getActiveSubscriptionInfoList();
                final int activeSubscriptionInfoCount = sub.getActiveSubscriptionInfoCount();
                SubscriptionInfo simInfo1 = (SubscriptionInfo) subscriptionInfos.get(0);
                SubscriptionInfo simInfo2 = (SubscriptionInfo) subscriptionInfos.get(1);
                SubscriptionInfo simInfo3 = (SubscriptionInfo) subscriptionInfos1.get(0);
                SubscriptionInfo simInfo4 = (SubscriptionInfo) subscriptionInfos1.get(1);
                SubscriptionManager.ACTION_REFRESH_SUBSCRIPTION_PLANS

                System.out.println("activeSubscriptionInfoCount = "+activeSubscriptionInfoCount);
                System.out.println("simInfo1 " + simInfo1.getSubscriptionId() + " , " + simInfo1.getNumber()
                        + " , " + simInfo1.getSubscriptionType()+
                        " "+simInfo1.getCarrierName()+" "+simInfo1.getSimSlotIndex()+ " "+simInfo1.getMccString()
                +" "+simInfo1.isOpportunistic());
                System.out.println("simInfo2 " + simInfo2.getSubscriptionId() + " , " + simInfo2.getNumber()
                        + " , " + simInfo2.getSubscriptionType()+
                        " "+simInfo2.getCarrierName()+" "+simInfo2.getSimSlotIndex()
                        + " "+simInfo2.getMccString()
                        +" "+simInfo2.isOpportunistic());
                System.out.println("simInfo3 " + simInfo3.getSubscriptionId() + " , " + simInfo3.getNumber()
                        + " , " + simInfo3.getSubscriptionType()+
                        " "+simInfo3.getCarrierName()+" "+simInfo3.getSimSlotIndex()+ " "+simInfo3.getMccString()
                        +" "+simInfo3.isOpportunistic());



                SmsManager.getSmsManagerForSubscriptionId(simInfo1.getSubscriptionId()).sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            } else {*/
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            //}
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private List<SubscriptionInfo> getServiceProviders(Context context) {
        List localList = null;
        SubscriptionManager localSubscriptionManager = SubscriptionManager.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return localList;
        }
        if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
            localList = localSubscriptionManager.getActiveSubscriptionInfoList();
            SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(0);
            SubscriptionInfo simInfo2 = (SubscriptionInfo) localList.get(1);
           //SendSMS From SIM One
            //SmsManager.getSmsManagerForSubscriptionId(simInfo1.getSubscriptionId()).sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

            //SendSMS From SIM Two
            //SmsManager.getSmsManagerForSubscriptionId(simInfo2.getSubscriptionId()).sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }

        return localList;
    }


    public interface SmsSenderBroadcast {
        public void onReceiveStatus(boolean smsDeliveredStatus);
    }
}

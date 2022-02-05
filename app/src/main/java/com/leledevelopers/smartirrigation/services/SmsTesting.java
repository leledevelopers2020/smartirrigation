package com.leledevelopers.smartirrigation.services;

import android.content.Context;
import android.widget.TextView;

public class SmsTesting {

    public SmsServices.SmsServiceBroadcast smsServiceBroadcast = null;
   public static Context contextPri;
   private SmsSender smsSender = new SmsSender();
    public void sendMessageBox(String phoneNumber, String message ,   double randomNumber, String screen_Specific_Sms) {

     //   System.out.println("---> " + smsReceiver.toString());
        smsSender.sendMessage(phoneNumber, message, this.contextPri /*view*/, screen_Specific_Sms);
        smsSender.setSmsSenderBroadcast(new SmsSender.SmsSenderBroadcast() {
            @Override
            public void onReceiveStatus(boolean smsDeliveredStatus) {
                System.out.println("service smsDeliveredStatus = " + smsDeliveredStatus);
                if (smsDeliveredStatus) {
                    //smsReceiver.waitFor_1_Minute(randomNumber,smsReceiver);
                } else {
                  //  enableViews();
                }
                smsServiceBroadcast.onReceiveSmsDeliveredStatus(smsDeliveredStatus,screen_Specific_Sms);
            }
        });
    }
    public void setSmsServiceBroadcast(SmsServices.SmsServiceBroadcast smsServiceBroadcast) {
        this.smsServiceBroadcast = smsServiceBroadcast;
    }
}

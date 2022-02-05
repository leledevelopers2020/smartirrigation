package com.leledevelopers.smartirrigation.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.leledevelopers.smartirrigation.models.BaseMessages;
import com.leledevelopers.smartirrigation.models.Message;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Narsing Rao.K
 */
public abstract class SmsServices extends AppCompatActivity {


    protected Context context;
    protected static String phoneNumber = "";
    protected CURD_Files<Message> createSMSFile = new CURD_FilesImpl<Message>();
    protected List<Message> messageList;
    protected BaseMessages baseMessages;
    public SmsServiceBroadcast smsServiceBroadcast = null;

    /**
     * This method should contain the code send SMS
     *
     * @return void
     */

     public void sendMessage(String phoneNumber, String message, TextView view, SmsReceiver smsReceiver, double randomNumber, String screen_Specific_Sms) {
        SmsSender smsSender = new SmsSender();
        System.out.println("---> " + smsReceiver.toString());
        smsSender.sendMessage(phoneNumber, message, this.context /*view*/, screen_Specific_Sms);
        smsSender.setSmsSenderBroadcast(new SmsSender.SmsSenderBroadcast() {
            @Override
            public void onReceiveStatus(boolean smsDeliveredStatus) {
                System.out.println("service smsDeliveredStatus = " + smsDeliveredStatus);
                if (smsDeliveredStatus) {
                    //smsReceiver.waitFor_1_Minute(randomNumber,smsReceiver);
                } else {
                    enableViews();
                }
                smsServiceBroadcast.onReceiveSmsDeliveredStatus(smsDeliveredStatus,screen_Specific_Sms);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private List<SubscriptionInfo> getServiceProviders(Context context) {
        List localList = null;
        SubscriptionManager localSubscriptionManager = SubscriptionManager.from(context);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return localList;
        }
        if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
            localList = localSubscriptionManager.getActiveSubscriptionInfoList();
            SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(0);
            SubscriptionInfo simInfo2 = (SubscriptionInfo) localList.get(1);
            System.out.println("simInfo1 " + simInfo1.getSubscriptionId() + " , " + simInfo1.getNumber() + " , " + simInfo1.getSubscriptionType());
            System.out.println("simInfo2 " + simInfo2.getSubscriptionId() + " , " + simInfo2.getNumber() + " , " + simInfo2.getSubscriptionType());
            //SendSMS From SIM One
            //SmsManager.getSmsManagerForSubscriptionId(simInfo1.getSubscriptionId()).sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

            //SendSMS From SIM Two
            //SmsManager.getSmsManagerForSubscriptionId(simInfo2.getSubscriptionId()).sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }

        return localList;
    }

    /**
     * This method should contain the code to initialize the UI Views
     *
     * @return void
     */
    abstract public void initViews();

    abstract public void enableViews();

    abstract public void disableViews();

    /**
     * This method access the required permissions
     *
     * @return void
     */
    public void accessPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                }, ProjectUtils.SMS_REQUEST_CODE);
    }

    /**
     * This method check for the permissions
     *
     * @return void
     */
    public boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) +
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) +
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) +
                ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) +
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) +
                ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            Toast.makeText(context, "Please provide all permissions", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    protected void readAllMessages() throws IOException, ClassNotFoundException {
        String date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());
        if (createSMSFile.isFileHasData(context, ProjectUtils.MESSAGES_PATH)) {
            baseMessages = (BaseMessages) createSMSFile.getFile(context, ProjectUtils.MESSAGES_PATH);
            if (!baseMessages.getLastAccessedDate().isEmpty()) {
                messageList = baseMessages.getMessages();

                messageList = getMessages(messageList, baseMessages);
                messageList = deleteMessages(messageList);
                baseMessages.setMessages(messageList);
                baseMessages.setLastAccessedDate(date);
                baseMessages.setMessageInitial(false);
                createSMSFile.createFile(context, ProjectUtils.MESSAGES_PATH, baseMessages);
            } else {
                messageList = new ArrayList<Message>();
                baseMessages = new BaseMessages();

                messageList = getMessages(messageList, baseMessages);
                messageList = deleteMessages(messageList);
                baseMessages.setMessages(messageList);
                baseMessages.setLastAccessedDate(date);
                baseMessages.setMessageInitial(false);
                createSMSFile.createFile(context, ProjectUtils.MESSAGES_PATH, baseMessages);
            }
        } else {
            messageList = new ArrayList<Message>();
            baseMessages = new BaseMessages();
            messageList = getMessages(messageList, baseMessages);
            baseMessages.setMessages(messageList);
            baseMessages.setLastAccessedDate(date);
            baseMessages.setMessageInitial(false);
            createSMSFile.createFile(context, ProjectUtils.MESSAGES_PATH, baseMessages);
        }
    }

    private List<Message> getMessages(List<Message> messages, BaseMessages baseMessages) {
        Cursor cursor = null;
        List<Message> newMessages = new ArrayList<Message>();
        String ownerNumber = SmsServices.phoneNumber.replaceAll("\\s", "");
        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        //messages.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String filter = getDateFilter(baseMessages.isMessageInitial(), baseMessages.getLastAccessedDate());
        cursor = context.getContentResolver().query(SMS_INBOX, null, filter, null, null);

        while (cursor.moveToNext()) {
            Message message = new Message();
            // Convert date to a readable format.
            Calendar calendar = Calendar.getInstance();
            String date = cursor.getString(cursor.getColumnIndex("date"));
            Long timestamp = Long.parseLong(date);
            calendar.setTimeInMillis(timestamp);
            Date finaldate = calendar.getTime();
            String smsDate = dateFormat.format(finaldate);
            String smsTime = timeFormat.format(finaldate);
            String smsDateTime = dateTimeFormatter.format(finaldate);
            String smsBody = cursor.getString(cursor.getColumnIndex("body"));
            String phoneNumber = cursor.getString(cursor.getColumnIndex("address"));
            if (phoneNumber.contains(ownerNumber)) {
                message.setAction(smsBody);
                message.setDate(smsDate);
                message.setTime(smsTime);
                message.setDateTime(smsDateTime);
                newMessages.add(message);
            }
        }
        messages.addAll(newMessages);
        return messages;
    }

    protected String getDateFilter(boolean isInitial, String lastModifiedDate) {
        Date dateStart = null;
        Date dateEnd = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar currentCal = Calendar.getInstance();
        String currentdate;
        try {
            if (isInitial && lastModifiedDate.isEmpty()) {
                currentdate = dateFormat.format(currentCal.getTime());
                currentCal.add(Calendar.DATE, -7);
                String pastDate = dateFormat.format(currentCal.getTime());
                dateStart = formatter.parse(pastDate + "T00:00:00");
                dateEnd = formatter.parse(currentdate + "T23:59:59");
            } else if (!isInitial && !lastModifiedDate.isEmpty()) {
                currentdate = formatter.format(currentCal.getTime());
                dateStart = formatter.parse(lastModifiedDate);
                dateEnd = formatter.parse(currentdate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "date>=" + dateStart.getTime() + " and date<=" + dateEnd.getTime();
    }

    protected List<Message> getSMS() throws IOException, ClassNotFoundException {
        if (createSMSFile.isFileHasData(context, ProjectUtils.MESSAGES_PATH)) {
            baseMessages = (BaseMessages) createSMSFile.getFile(context, ProjectUtils.MESSAGES_PATH);
            this.messageList = baseMessages.getMessages();
            return this.messageList;
        } else
            return null;
    }

    protected List<Message> deleteMessages(List<Message> messages) {
        List<Message> updatedList = new ArrayList<Message>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currentCal = Calendar.getInstance();
        String currentdate = dateFormat.format(currentCal.getTime());
        try {
            Date endDate = dateFormat.parse(currentdate);
            for (Message message : messages) {
                if (Math.abs(dateFormat.parse(message.getDate()).getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24) <= 7
                        || message.getAction().contains(SmsUtils.RTC_BATTERY_FULL_STATUS)
                        || message.getAction().contains(SmsUtils.RTC_BATTERY_LOW_STATUS)) {
                    updatedList.add(message);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return updatedList;
    }

    public interface SmsServiceBroadcast {
        public void onReceiveSmsDeliveredStatus(boolean smsDeliveredStatus, String message);
    }

    public void setSmsServiceBroadcast(SmsServiceBroadcast smsServiceBroadcast) {
        this.smsServiceBroadcast = smsServiceBroadcast;
    }
}

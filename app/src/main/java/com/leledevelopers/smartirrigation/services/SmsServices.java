package com.leledevelopers.smartirrigation.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.leledevelopers.smartirrigation.models.BaseMessages;
import com.leledevelopers.smartirrigation.models.Message;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.starter.splashScreen;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;

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
    protected Handler handler = new Handler();

    protected Context context;
    protected static String phoneNumber = "";
    protected CURD_Files<Message> createSMSFile = new CURD_FilesImpl<Message>();
    protected List<Message> messageList;
    protected BaseMessages baseMessages;

    /**
     * This method should contain the code send SMS
     *
     * @return void
     */
    public void sendMessage(String phoneNumber, String message) {
        //   String phoneNumber=txt_pNumber.getText().toString().trim();
        //    String Message=txt_message.getAccessibilityClassName().toString().trim();
        if (!phoneNumber.equals("") || message != null || !message.equals("")) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
    }

    /**
     * This method should contain the code to initialize the UI Views
     *
     * @return void
     */
    abstract public void initViews();

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
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            Toast.makeText(context, "Please provide all permissions", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    protected void readAllMessages() throws IOException, ClassNotFoundException {
        if (createSMSFile.isFileHasData(context, ProjectUtils.MESSAGES_PATH)) {
            System.out.println("Has Data");
            baseMessages = (BaseMessages) createSMSFile.getFile(context, ProjectUtils.MESSAGES_PATH);
            if (!baseMessages.getLastAccessedDate().equals("")) {
                messageList = baseMessages.getMessages();
                System.out.println("messageList.size() = " + messageList.size());
                System.out.println(baseMessages.getLastAccessedDate());

                messageList = getMessages(messageList, baseMessages);
                baseMessages.setMessages(messageList);
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                baseMessages.setLastAccessedDate(date);
                createSMSFile.createFile(context, ProjectUtils.MESSAGES_PATH, baseMessages);
            }
        } else {
            System.out.println("Has Empty Data");
            messageList = new ArrayList<Message>();
            baseMessages = new BaseMessages();
            messageList = getMessages(messageList, baseMessages);
            System.out.println("messageList.size()--> " + messageList.size());

            baseMessages.setMessages(messageList);
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            baseMessages.setLastAccessedDate(date);
            createSMSFile.createFile(context, ProjectUtils.MESSAGES_PATH, baseMessages);
            System.out.println("baseMessages.toString()--> " + baseMessages.toString());
        }
    }

    private List<Message> getMessages(List<Message> messages, BaseMessages baseMessages) {
        Cursor cursor = null;
        String ownerNumber = SmsServices.phoneNumber.replaceAll("\\s", "");
        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        messages.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String filter = getDateFilter();
        cursor = context.getContentResolver().query(SMS_INBOX, null, filter, null, null);

        while (cursor.moveToNext()) {
            Message message = new Message();
            // Convert date to a readable format.
            Calendar calendar = Calendar.getInstance();
            String date = cursor.getString(cursor.getColumnIndex("date"));
            System.out.println("date---> " + date);
            Long timestamp = Long.parseLong(date);
            calendar.setTimeInMillis(timestamp);
            Date finaldate = calendar.getTime();
            String smsDate = dateFormat.format(finaldate);
            String smsTime = timeFormat.format(finaldate);
            String smsBody = cursor.getString(cursor.getColumnIndex("body"));
            String phoneNumber = cursor.getString(cursor.getColumnIndex("address"));
            if (ownerNumber.equals(phoneNumber)) {
                message.setAction(smsBody);
                message.setDate(smsDate);
                message.setTime(smsTime);
                messages.add(message);
            }
        }
        return messages;
    }

    protected String getDateFilter() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar currentCal = Calendar.getInstance();
        String currentdate = dateFormat.format(currentCal.getTime());
        currentCal.add(Calendar.DATE, -7);
        String pastDate = dateFormat.format(currentCal.getTime());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date dateStart = null;
        Date dateEnd = null;
        try {
            dateStart = formatter.parse(pastDate + "T00:00:00");
            dateEnd = formatter.parse(currentdate + "T23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "date>=" + dateStart.getTime() + " and date<=" + dateEnd.getTime();
    }

    protected List<Message> getSMS() throws IOException, ClassNotFoundException {
        if (createSMSFile.isFileHasData(context, ProjectUtils.MESSAGES_PATH)) {
            baseMessages = (BaseMessages) createSMSFile.getFile(context, ProjectUtils.MESSAGES_PATH);
            this.messageList = baseMessages.getMessages();
            System.out.println("messageList.size() = " + messageList.size());
            System.out.println(baseMessages.getLastAccessedDate());
            return this.messageList;
        } else
            return null;
    }

    /*public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
*/

}
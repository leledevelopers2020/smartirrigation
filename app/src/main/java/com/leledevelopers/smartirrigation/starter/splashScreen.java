package com.leledevelopers.smartirrigation.starter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.leledevelopers.smartirrigation.R;
import com.leledevelopers.smartirrigation.Screen_5;
import com.leledevelopers.smartirrigation.models.BaseMessages;
import com.leledevelopers.smartirrigation.models.Message;
import com.leledevelopers.smartirrigation.registration.Screen_1;
import com.leledevelopers.smartirrigation.MainActivity_GSM;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class splashScreen extends SmsServices {
    boolean registered; ///this value is based on database if available it is set to true or else set to false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        File file = new File(splashScreen.this.getExternalFilesDir(null) + ProjectUtils.FILE_PATH);
        if (file.exists()) {
            this.context = getApplicationContext();
            readUserFile();
            registered = true;
        } else {
            registered = false;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (registered) {
                    Intent intent = new Intent(splashScreen.this,
                            MainActivity_GSM.class);                     // if registered with GSM it goes to screen_3_1 or else if with wi-fi goes to screen_3_2
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(splashScreen.this,
                            Screen_1.class);                                //if not registered goes to registration page
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);

    }

    /**
     * This method should contain the code to initialize the UI Views
     *
     * @return void
     */
    @Override
    public void initViews() {
        //nothing
    }

    private void readUserFile() {
        File file = new File(splashScreen.this.getExternalFilesDir(null) + ProjectUtils.FILE_PATH);
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line);
                }
                //String[] s = text.toString().split("[#]");
                SmsServices.phoneNumber = text.toString();
                if (!SmsServices.phoneNumber.equals("")) {
                    readMessages();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println(e.fillInStackTrace());
            }
        }
    }

    private void readMessages() throws IOException, ClassNotFoundException {
        CURD_Files<Message> curd_files = new CURD_FilesImpl<Message>();
        List<Message> messageList;
        BaseMessages baseMessages;

        if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.MESSAGES_PATH)) {
            System.out.println("Has Data");
            baseMessages = (BaseMessages) curd_files.getFile(splashScreen.this, ProjectUtils.MESSAGES_PATH);
            if(!baseMessages.getLastAccessedDate().equals("")){
                messageList = baseMessages.getMessages();
                System.out.println("messageList.size() = "+messageList.size());
                System.out.println(baseMessages.getLastAccessedDate());

                /*messageList = getMessages(messageList,baseMessages);
                baseMessages.setMessages(messageList);
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                baseMessages.setLastAccessedDate(date);
                curd_files.createFile(splashScreen.this, ProjectUtils.MESSAGES_PATH, baseMessages);*/
            }
        } else {
            System.out.println("Has Empty Data");
            messageList = new ArrayList<Message>();
            baseMessages = new BaseMessages();
            messageList = getMessages(messageList,baseMessages);
            System.out.println("messageList.size()--> "+messageList.size());

            baseMessages.setMessages(messageList);
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            baseMessages.setLastAccessedDate(date);
            curd_files.createFile(splashScreen.this, ProjectUtils.MESSAGES_PATH, baseMessages);
            System.out.println("baseMessages.toString()--> "+baseMessages.toString());
        }
    }

    private List<Message> getMessages(List<Message> messages, BaseMessages baseMessages){
        Cursor cursor = null;
        String ownerNumber = SmsServices.phoneNumber.replaceAll("\\s", "");
        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");

        if(baseMessages.getLastAccessedDate().equals("")){
            cursor = getContentResolver().query(SMS_INBOX, null, null, null, null);
        } else {

        }


        while (cursor.moveToNext()) {
            Message message = new Message();
            // Convert date to a readable format.
            Calendar calendar = Calendar.getInstance();
            String date = cursor.getString(cursor.getColumnIndex("date"));
            System.out.println("date---> "+date);
            Long timestamp = Long.parseLong(date);
            calendar.setTimeInMillis(timestamp);
            Date finaldate = calendar.getTime();
            String smsDate = finaldate.toString();
            String smsBody = cursor.getString(cursor.getColumnIndex("body"));
            String phoneNumber = cursor.getString(cursor.getColumnIndex("address"));
            if (ownerNumber.equals(phoneNumber) && smsBody.contains("field no")) {
                message.setAction(smsBody);
                message.setDate(smsDate);
                messages.add(message);
            }
        }
        return messages;
    }
}
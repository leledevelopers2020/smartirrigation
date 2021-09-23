package com.leledevelopers.smartirrigation.registration;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.leledevelopers.smartirrigation.R;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Screen_2_1 extends SmsServices {
    private static final String TAG = Screen_2_1.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private TextView oldPassword, newPassword, status;
    private Button gsmContact, set;
    private final String fileName = "details.txt";
    private final String filePath = "MyFileDir";
    String filePhoneNumber = "9912473753";
    String filePassword = "psw";
    private Boolean b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen21);
        initViews();
        this.context = getApplicationContext();
        gsmContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent, ProjectUtils.PICK_CONTACT);
                }
            }
        });
        if (!externalStorageIsAvailableForRW()) {
            set.setEnabled(false);
        }
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SmsReceiver", "on click");
                smsReceiver.waitFor_1_Minute();
                b = true;
                if (getPhoneNumber() != null && filePassword != null) {
                    File myExternalFile = new File(getExternalFilesDir(ProjectUtils.DIRECTORY_PATH), ProjectUtils.FILE_NAME);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(myExternalFile);
                        String data = getPhoneNumber()+"#"+filePassword;
                        fos.write(data.getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Screen_2_1.this, "Your data has been stored successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Screen_2_1.this, "PLease select proper GSM number or enter correct otp/old password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean externalStorageIsAvailableForRW() {
        String extStorageState = Environment.getExternalStorageState();
        if (extStorageState.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }


    @Override
    public void initViews() {
        gsmContact = findViewById(R.id.screen_2_1_button_1);
        oldPassword = findViewById(R.id.screen_2_1_textview_1);
        newPassword = findViewById(R.id.screen_2_1_textview_2);
        set = findViewById(R.id.screen_2_1_button_2);
        status = findViewById(R.id.screen_2_1_status);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor phone = getContentResolver().query(contactData, null, null, null, null);
            if (phone.moveToFirst()) {
                String contactNumberName = phone.getString(phone.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                setPhoneNumber(contactNumber);
                gsmContact.setText(contactNumberName + " - " + contactNumber);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsReceiver.setContext(getApplicationContext());
        smsReceiver.startBroadcastReceiver();
        smsReceiver.setSmsMessageBroadcast(new SmsReceiver.SmsReceiverBroadcast() {
            @Override
            public void onReceiveSms(String phoneNumber, String message) {
                b = false;
                Log.d("SmsReceiver", "Yup got it!! " + phoneNumber + " , " + message);
                status.setText("Screen 2.1\nSender's Number = " + phoneNumber + "\n Message : " + message);
                checkSMS(message);
            }

            @Override
            public void checkTime(String time) {
                Log.d("SmsReceiver", "new time " + time);
                if (b) {
                    status.setText("System Down");
                }
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();

    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
    }

    public void checkSMS(String message) {
        switch (message) {
            case SmsUtils.INSMS_1_1: {
                startActivity(new Intent(Screen_2_1.this, Screen_9.class));
                break;
            }
            case SmsUtils.INSMS_1_2: {
                Log.d("SmsReceiver", "true");
                oldPassword.requestFocus();
                break;
            }
            case SmsUtils.INSMS_3_1: {
                status.setText("Password Updated successfully");
                startActivity(new Intent(Screen_2_1.this, Screen_3_1.class));
                break;
            }
        }
    }

}
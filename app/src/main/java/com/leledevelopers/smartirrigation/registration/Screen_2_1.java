package com.leledevelopers.smartirrigation.registration;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.leledevelopers.smartirrigation.MainActivity_GSM;
import com.leledevelopers.smartirrigation.R;
import com.leledevelopers.smartirrigation.Screen_9;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
    private boolean isSetClicked = false;
    private CheckBox checkbox1,checkbox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen21);
        initViews();
        Toast.makeText(Screen_2_1.this, TAG, Toast.LENGTH_LONG).show();
        System.out.println(TAG + " into");
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

                smsReceiver.waitFor_1_Minute();
                b = true;

                if (SmsServices.phoneNumber != null && filePassword != null) {
                    File myExternalFile = new File(getExternalFilesDir(ProjectUtils.DIRECTORY_PATH), ProjectUtils.FILE_NAME);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(myExternalFile);
                        String data = SmsServices.phoneNumber + "#" + filePassword;
                        fos.write(data.getBytes());
                        isSetClicked = true;
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
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
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
        oldPassword = findViewById(R.id.screen_2_1_edittext_1);
        newPassword = findViewById(R.id.screen_2_1_edittext_2);
        set = findViewById(R.id.screen_2_1_button_2);
        status = findViewById(R.id.screen_2_1_status);
        checkbox1=findViewById(R.id.checkbox1);
        checkbox2=findViewById(R.id.checkbox2);
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
                SmsServices.phoneNumber = contactNumber;
                gsmContact.setText(contactNumberName + " - " + contactNumber);
            }
        }
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
                try {
                    createConfgFiles();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(Screen_2_1.this, MainActivity_GSM.class));
                break;
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
                System.out.println("Screen 2.1\nSender's Number = " + phoneNumber + "\n Message : " + message);
                status.setText("Screen 2.1\nSender's Number = " + phoneNumber + "\n Message : " + message);
                if (SmsServices.phoneNumber.replaceAll("\\s", "").equals(phoneNumber.replaceAll("\\s", "")) && isSetClicked) {
                    checkSMS(message);
                }
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

    private void createConfgFiles() throws IOException {
        CURD_Files curd_files = new CURD_FilesImpl();
        if (!curd_files.isFileExists(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_IRRIGATION_NAME)) {
            curd_files.createEmptyFile(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_IRRIGATION_NAME);
        }
        if (!curd_files.isFileExists(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_FERTIGATION_NAME)) {
            curd_files.createEmptyFile(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_FERTIGATION_NAME);
        }
        if (!curd_files.isFileExists(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_FILTRATION_NAME)) {
            curd_files.createEmptyFile(getApplicationContext(), ProjectUtils.CONFG_DIRECTORY_PATH, ProjectUtils.CONFG_FILTRATION_NAME);
        }
    }

}
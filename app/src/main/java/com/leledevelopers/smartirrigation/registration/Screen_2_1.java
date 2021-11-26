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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.leledevelopers.smartirrigation.starter.splashScreen;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class Screen_2_1 extends SmsServices {
    private static final String TAG = Screen_2_1.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();

    private EditText oldPassword, newPassword;
    private TextView status;
    private Button gsmContact, set;
    private final String fileName = "details.txt";
    private final String filePath = "MyFileDir";
    /*String filePhoneNumber = "9912473753";
    String filePassword = "psw";*/
    private Boolean b, systemDown = false;
    SmsUtils smsUtils = new SmsUtils();
    private boolean isSetClicked = false, isGSMSelected = false, isPasswordSaved = false;
    private CheckBox checkbox1, checkbox2;
    private String smsData;

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
                    isGSMSelected = false;
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                    startActivityForResult(intent, ProjectUtils.PICK_CONTACT);
                }
            }
        });
        if (!externalStorageIsAvailableForRW()) {
            set.setEnabled(false);
        }
        oldPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword.setCursorVisible(true);
            }
        });
        newPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword.setCursorVisible(true);
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status.setText("");

                try {

                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    oldPassword.clearFocus();
                    newPassword.clearFocus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isGSMSelected) {
                    if (!systemDown) {

                        if (validateInput(oldPassword.getText().toString(), newPassword.getText().toString())) {
                            smsReceiver.waitFor_1_Minute();
                            b = true;
                            isSetClicked = true;
                            cursorVisibility();
                            smsData = smsUtils.OutSMS_1(oldPassword.getText().toString(), newPassword.getText().toString());
                            sendMessage(SmsServices.phoneNumber, smsData);
                            status.setText("Message Sent");
                         } else {
                            focus(oldPassword.getText().toString(), newPassword.getText().toString());
                        }
                    }
                } else {
                    Toast.makeText(Screen_2_1.this, "Please select the phone number!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
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
                if (isChecked) {
                    // show password
                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
      newPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    validate(newPassword.getText().toString(),"Newpassword");

                }
            }
        });
      oldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    validate(oldPassword.getText().toString(),"Oldpassword");

                }
            }
        });
      newPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
          @Override
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
              if(actionId==EditorInfo.IME_ACTION_DONE)
              {

              }
              return true;
          }
      });
      newPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
          @Override
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
              if(actionId==EditorInfo.IME_ACTION_DONE)
              {
                  status.setText("Status");

                  try {

                      InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                      imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                  } catch (Exception e) {
                      // TODO: handle exception
                  }
                  newPassword.clearFocus();
              }
              return true;
          }
      });

    }



    private boolean validateInput(String oldPasswordlocal, String newPasswordlocal) {
        boolean matching = false;
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        if (p.matcher(oldPasswordlocal).matches() && p.matcher(newPasswordlocal).matches()
                && oldPasswordlocal.length() == 6 && newPasswordlocal.length() == 6 && !(oldPasswordlocal.equals(newPasswordlocal))) {
            matching = true;
        }
        else {
            status.setText("Status");
        }
        return matching;
    }
    private void validate(String input,String editTextField )
    {
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        if (!(p.matcher(input).matches() && input.length() == 6))
        {
            switchFocus(editTextField);
        }
        else {
            status.setText("Status");
        }

    }

    private void switchFocus(String editTextField) {
        switch (editTextField)
        {
            case "Oldpassword": {

                oldPassword.getText().clear();
                oldPassword.setError("Enter valid 6 digit password");
                status.setText("Enter valid Old/Factory password");
            }
                break;
            case "Newpassword":
            {

                newPassword.getText().clear();
                newPassword.setError("Enter valid 6 digit password");
                status.setText("Enter valid new password");
            }
            break;
            case "Same":
            {   
                status.setText("Both Passwords cannot be same");
            }
            break;
        }
    }

    private void focus(String oldPasswordlocal, String newPasswordlocal) {
        String regex = "[0-9]+";

        if(oldPasswordlocal.length()!=6 && !(oldPasswordlocal.matches(regex)))
        {
            oldPassword.getText().clear();
            oldPassword.setError("Enter valid 6 digit password");
            status.setText("Enter valid Old/Factory password");
        }
        if(newPasswordlocal.length()!=6 &&!(newPasswordlocal.matches(regex)))
        {
            newPassword.getText().clear();
            newPassword.setError("Enter valid 6 digit password");
            status.setText("Enter valid new password");
        }
        if (oldPasswordlocal.equals(newPasswordlocal)) {
            oldPassword.getText().clear();
            newPassword.getText().clear();
            status.setText("Both Passwords cannot be same");
        }
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
        checkbox1 = findViewById(R.id.checkbox1);
        checkbox2 = findViewById(R.id.checkbox2);
        File file = new File(Screen_2_1.this.getExternalFilesDir(null) + ProjectUtils.FILE_PATH);
        if (file.exists()) {
            gsmContact.setText(SmsServices.phoneNumber);
        }
    }
    private void cursorVisibility() {
        try{
            oldPassword.setCursorVisible(false);
            newPassword.setCursorVisible(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
                isGSMSelected = true;
            }
        }
    }

    public void checkSMS(String message) {
        System.out.println("message--> " +message);
        switch (message) {
            case SmsUtils.INSMS_1_1: {
                status.setText("Admin set successfully");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isPasswordSaved = true;
                        startActivity(new Intent(Screen_2_1.this, MainActivity_GSM.class));
                        finish();
                    }
                }, 1000);
                break;
            }
            case SmsUtils.INSMS_1_2: {
                status.setText("Wrong factory Password");
                oldPassword.requestFocus();
                break;
            }
            case SmsUtils.INSMS_3_1: {
                status.setText("Password Updated successfully");
                isPasswordSaved = true;
                try {
                    saveFileDetails();
                    createConfgFiles();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Screen_2_1.this, MainActivity_GSM.class));
                        finish();
                    }
                }, 1000);
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!SmsServices.phoneNumber.equals("")) {
            System.out.println("has phone number = "+SmsServices.phoneNumber);
            isGSMSelected = true;
        } else {
            System.out.println("phone number = " + SmsServices.phoneNumber);
        }
        smsReceiver.setContext(getApplicationContext());
        smsReceiver.startBroadcastReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
        smsReceiver.setSmsMessageBroadcast(new SmsReceiver.SmsReceiverBroadcast() {
            @Override
            public void onReceiveSms(String phoneNumber, String message) {
                b = false;
                System.out.println("isSetClicked  = " + isSetClicked + "\n systemDown : " + systemDown +" SmsServices.phoneNumber "+SmsServices.phoneNumber + " phoneNumber "+phoneNumber);
                if (SmsServices.phoneNumber.replaceAll("\\s", "").equals(phoneNumber.replaceAll("\\s", "")) && isSetClicked && !systemDown) {
                    System.out.println("Screen 2.1\nSender's Number = " + phoneNumber + "\n Message : " + message);
                    checkSMS(message);
                } else if(phoneNumber.contains(SmsServices.phoneNumber.replaceAll("\\s","")) && isSetClicked && !systemDown) {
                    System.out.println("Screen 2.1\nSender's Number = " + phoneNumber + "\n Message : " + message);
                    checkSMS(message);
                }
            }

            @Override
            public void checkTime(String time) {
                if (b) {
                    systemDown = true;
                    smsReceiver.unRegisterBroadCasts();
                    status.setText("System Down");
                }
            }

        });
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
        if (!curd_files.isFileExists(getApplicationContext(), ProjectUtils.DIRECTORY_PATH, ProjectUtils.MESSAGES_FILE)) {
            curd_files.createEmptyFile(getApplicationContext(), ProjectUtils.DIRECTORY_PATH, ProjectUtils.MESSAGES_FILE);
        }
    }

    private void saveFileDetails() {
        if (!SmsServices.phoneNumber.equals("")) {
            File myExternalFile = new File(getExternalFilesDir(ProjectUtils.DIRECTORY_PATH), ProjectUtils.FILE_NAME);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(myExternalFile);
                String data = SmsServices.phoneNumber;
                fos.write(data.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(Screen_2_1.this, "PLease select proper GSM number or enter correct otp/old password", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!isPasswordSaved){
            SmsServices.phoneNumber = "";
            isSetClicked = false;
            isGSMSelected = false;
            isPasswordSaved = false;
        }
    }
}
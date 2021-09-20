package com.leledevelopers.smartirrigation.registration;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.leledevelopers.smartirrigation.R;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Screen_2_1 extends SmsServices {
    private static final String TAG = Screen_2_1.class.getSimpleName();
    private TextView oldPassword, newPassword, status;
    private Button gsmContact, set;
    private final String fileName="details.txt";
    private final String filePath="MyFileDir";
    String filePhoneNumber="9912473753";
    String filePassword="asdfgh";
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
        if(!externalStorageIsAvailableForRW())
        {
            set.setEnabled(false);
        }
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePhoneNumber!=null && filePassword!=null)
                {
                    File myExternalFile=new File(getExternalFilesDir(filePath),fileName);
                    FileOutputStream fos=null;
                    try {
                          fos=new FileOutputStream(myExternalFile);
                          fos.write(filePhoneNumber.getBytes());
                          fos.write(filePassword.getBytes());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Screen_2_1.this,"Your data has been stored successfully",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Screen_2_1.this,"PLease enter the data",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean externalStorageIsAvailableForRW()
    {
        String extStorageState= Environment.getExternalStorageState();
        if(extStorageState.equals(Environment.MEDIA_MOUNTED)){
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
                gsmContact.setText(contactNumberName + " - " + contactNumber);
            }
        }
    }
}
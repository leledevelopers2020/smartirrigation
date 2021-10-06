package com.leledevelopers.smartirrigation.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.leledevelopers.smartirrigation.utils.ProjectUtils;

/**
 * @author Narsing Rao.K
 */
public abstract class SmsServices extends AppCompatActivity {
    protected Context context;
    protected static String phoneNumber;

    /**
     * This method should contain the code send SMS
     *
     * @return void
     */
    public void sendMessage(String phoneNumber,String message)
    {
     //   String phoneNumber=txt_pNumber.getText().toString().trim();
    //    String Message=txt_message.getAccessibilityClassName().toString().trim();
        if(!phoneNumber.equals("") || message!=null || !message.equals("")) {
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
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)+
                ActivityCompat.checkSelfPermission(context,Manifest.permission.SEND_SMS)+
                ActivityCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE)+
                ActivityCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            Toast.makeText(context, "Please provide all permissions", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    /*public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
*/

}

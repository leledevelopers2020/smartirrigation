package com.leledevelopers.smartirrigation.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.leledevelopers.smartirrigation.utils.ProjectUitls;

/**
 * @author Narsing Rao.K
 */
public abstract class SmsServices extends AppCompatActivity {
    protected Context context;

    /**
     * This method should contain the code send SMS
     *
     * @return void
     */
    abstract public void sendMessage();

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
                }, ProjectUitls.SMS_REQUEST_CODE);
    }

    /**
     * This method check for the permissions
     *
     * @return void
     */
    public boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) +
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) +
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            Toast.makeText(context, "Please provide all permissions", Toast.LENGTH_LONG).show();
            return false;
        }

    }
}

package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.registration.Screen_2_1;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.text.DecimalFormat;
import java.util.Calendar;

public class Screen_9 extends SmsServices {
    private static final String TAG = Screen_9.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private SmsUtils smsUtils = new SmsUtils();
    private Boolean b, systemDown = false;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button setSystemTime, getSystemTime, updatePassword, setMotorloadCutoff, back_9, save;
    private TextView status;
    DecimalFormat mFormat = new DecimalFormat("00");
    Calendar calendar = Calendar.getInstance();
    private double randomNumber;
    private boolean isSetTimeClicked = false;
    private boolean isGetTimeClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen9);
        this.context = getApplicationContext();
        initViews();
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.languagesArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        setMotorloadCutoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_9.this, Screen_10.class));
            }
        });
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                Intent intent = new Intent(Screen_9.this, Screen_2_1.class);
                intent.putExtra("Settings", true);
                startActivity(intent);
                finish();
            }
        });
        setSystemTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                randomNumber = Math.random();
                //smsReceiver.waitFor_1_Minute(randomNumber);
                //b = true;
                isSetTimeClicked = true;
                String smsData = smsUtils.OutSMS_10(mFormat.format(Double.valueOf(calendar.get(Calendar.DATE))) + "", mFormat.format(Double.valueOf((calendar.get(Calendar.MONTH) + 1))) + "",
                        mFormat.format(Double.valueOf((calendar.get(Calendar.YEAR)) % 100)) + "", mFormat.format(Double.valueOf(calendar.get(Calendar.HOUR_OF_DAY))) + ""
                        , mFormat.format(Double.valueOf(calendar.get(Calendar.MINUTE))) + "", mFormat.format(Double.valueOf(calendar.get(Calendar.SECOND))) + "");

                /*String smsData=smsUtils.OutSMS_10(  calendar.get(Calendar.DATE)+"",(calendar.get(Calendar.MONTH)+1)+"",
                        (calendar.get(Calendar.YEAR))%100+"",calendar.get(Calendar.HOUR_OF_DAY)+""
                        ,calendar.get(Calendar.MINUTE)+"", calendar.get(Calendar.SECOND)+"");*/
                sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber);
                status.setText("Date Timestamp SMS Sent");
            }
        });
        getSystemTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableViews();
                randomNumber = Math.random();
                //smsReceiver.waitFor_1_Minute(randomNumber);
                //b = true;
                isGetTimeClicked = true;
                String smsData = smsUtils.OutSMS_11;
                sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber);
                status.setText("Get System Timestamp SMS Sent");
            }
        });
        back_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                startActivity(new Intent(Screen_9.this, Screen_4.class));
                finish();
            }
        });
    }


    @Override
    public void initViews() {
        spinner = (Spinner) findViewById(R.id.language_spinner);
        setSystemTime = findViewById(R.id.setSystemTime);
        getSystemTime = findViewById(R.id.getSystemTime);
        updatePassword = findViewById(R.id.updatePassword);
        setMotorloadCutoff = findViewById(R.id.setMotorloadCutoff);
        status = findViewById(R.id.screen_9_status);
        save = findViewById(R.id.save);
        back_9 = findViewById(R.id.back_9);
    }

    @Override
    public void enableViews() {
        spinner.setEnabled(true);
        setSystemTime.setEnabled(true);
        getSystemTime.setEnabled(true);
        updatePassword.setEnabled(true);
        setMotorloadCutoff.setEnabled(true);
    }

    @Override
    public void disableViews() {
        spinner.setEnabled(false);
        setSystemTime.setEnabled(false);
        getSystemTime.setEnabled(false);
        updatePassword.setEnabled(false);
        setMotorloadCutoff.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsReceiver.setContext(getApplicationContext());
        smsReceiver.startBroadcastReceiver();
        smsReceiver.setSmsMessageBroadcast(new SmsReceiver.SmsReceiverBroadcast() {
            @Override
            public void onReceiveSms(String phoneNumber, String message) {
                if (SmsServices.phoneNumber.replaceAll("\\s", "").equals(phoneNumber.replaceAll("\\s", "")) && !systemDown) {
                    checkSMS(message);
                } else if (phoneNumber.contains(SmsServices.phoneNumber.replaceAll("\\s", "")) && !systemDown) {
                    checkSMS(message);
                }
            }

            @Override
            public void checkTime(double randomValue) {
                if (b && (randomNumber == randomValue)) {
                    enableViews();
                    systemDown = true;
                    smsReceiver.unRegisterBroadCasts();
                    status.setText(SmsUtils.SYSTEM_DOWN);
                    startActivity(new Intent(Screen_9.this, MainActivity_GSM.class));
                    finish();
                }
            }

        });

        this.setSmsServiceBroadcast(new SmsServiceBroadcast() {
            @Override
            public void onReceiveSmsDeliveredStatus(boolean smsDeliveredStatus) {
                System.out.println("non service page smsDeliveredStatus - " + smsDeliveredStatus);
                if (smsDeliveredStatus) {
                    b = true;
                } else {
                    isGetTimeClicked = false;
                    isSetTimeClicked = false;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Screen_9.this, Screen_4.class));
        finish();
    }

    public void checkSMS(String message) {
        if (message.toLowerCase().contains(SmsUtils.INSMS_10_1.toLowerCase()) && isSetTimeClicked) {
            b = false;
            isSetTimeClicked = false;
            status.setText("System time set to current time");
            enableViews();
        } else if (message.toLowerCase().contains(SmsUtils.INSMS_10_2.toLowerCase())) {
            b = false;
            status.setText("Failed to set system time, please set system time again");
            enableViews();
        } else if (message.toLowerCase().contains(SmsUtils.INSMS_11_1.toLowerCase()) && isGetTimeClicked) {
            b = false;
            isGetTimeClicked = false;
            status.setText(SmsUtils.INSMS_11_1 + (mFormat.format(Double.valueOf(calendar.get(Calendar.DATE))) + "/" + mFormat.format(Double.valueOf((calendar.get(Calendar.MONTH) + 1))) + "/"
                    + mFormat.format(Double.valueOf((calendar.get(Calendar.YEAR)) % 100))));
            enableViews();
        }
    }
}
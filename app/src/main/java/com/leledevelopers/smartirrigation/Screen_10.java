package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

public class Screen_10 extends SmsServices {
    private static final String TAG = Screen_10.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private SmsUtils smsUtils = new SmsUtils();
    private Boolean b, systemDown = false;
    EditText noLoadCutoffText, fullLoadCutOffText;
    private Button setMotorLoadThreshold, back_10;
    private TextView status;
    private boolean validate = true;
    private double randomNumber;
    private boolean isSetMotorLoadClicked = false;
    private static boolean screen_10_Visible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen10);
        this.context = getApplicationContext();
        initViews();
        setMotorLoadThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursorVisibility();
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (validateInput(noLoadCutoffText.getText().toString(), fullLoadCutOffText.getText().toString()) && !systemDown) {
                    disableViews();
                    randomNumber = Math.random();
                    String smsData = smsUtils.OutSMS_12(noLoadCutoffText.getText().toString(),
                            fullLoadCutOffText.getText().toString());
                    sendMessage(SmsServices.phoneNumber, smsData, status, smsReceiver, randomNumber, "Motorload threshold settings SMS ");
                    isSetMotorLoadClicked = true;
                }
            }
        });
        back_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                startActivity(new Intent(Screen_10.this, Screen_9.class));
                finish();
            }
        });
        noLoadCutoffText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noLoadCutoffText.setCursorVisible(true);
            }
        });
        fullLoadCutOffText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullLoadCutOffText.setCursorVisible(true);
            }
        });
        noLoadCutoffText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (noLoadCutoffText.getText().toString().length() == 0 ||
                            !(validateRange(0, 1024, Integer.parseInt(noLoadCutoffText.getText().toString())))) {
                        noLoadCutoffText.getText().clear();
                        noLoadCutoffText.setError("Enter a valid value");

                    }
                }
            }
        });
        fullLoadCutOffText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Log.d("tag", fullLoadCutOffText.getText().toString() + "full " + fullLoadCutOffText.getText().toString().length());
                    if (fullLoadCutOffText.getText().toString().length() == 0 ||
                            !(validateRange(0, 1024, Integer.parseInt(fullLoadCutOffText.getText().toString())))) {

                        fullLoadCutOffText.getText().clear();
                        fullLoadCutOffText.setError("Enter a valid value");
                    }
                }
            }
        });
        fullLoadCutOffText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    fullLoadCutOffText.clearFocus();
                }
                return true;
            }
        });
    }


    private boolean validateInput(String noLoadCutoffTextlocal, String fullLoadCutOffTextlocal) {
        Log.d("tag", fullLoadCutOffTextlocal + "full   " + noLoadCutoffTextlocal);

        try {
            if (noLoadCutoffTextlocal.equals("") || !(validateRange(0, 1024, Integer.parseInt(noLoadCutoffTextlocal)))) {
                noLoadCutoffText.getText().clear();
                noLoadCutoffText.setError("Enter a valid value");
                validate = false;
            }
            if (fullLoadCutOffTextlocal.equals("") || !(validateRange(0, 1024, Integer.parseInt(fullLoadCutOffTextlocal)))) {

                fullLoadCutOffText.getText().clear();
                fullLoadCutOffText.setError("Enter a valid value");
                validate = false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return validate;
    }

    private boolean validateRange(int min, int max, int inputValue) {
        if (inputValue >= min && inputValue <= max) {
            return true;
        }
        return false;
    }

    private void cursorVisibility() {
        try {
            noLoadCutoffText.setCursorVisible(false);
            fullLoadCutOffText.setCursorVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initViews() {
        noLoadCutoffText = findViewById(R.id.noLoadCutoffText);
        fullLoadCutOffText = findViewById(R.id.fullLoadCutOffText);
        setMotorLoadThreshold = findViewById(R.id.setMotorLoadThreshold);
        status = findViewById(R.id.screen_10_status);
        back_10 = findViewById(R.id.back_10);
    }

    @Override
    public void enableViews() {
        noLoadCutoffText.setFocusableInTouchMode(true);
        fullLoadCutOffText.setFocusableInTouchMode(true);
        //disable views
        noLoadCutoffText.setEnabled(true);
        fullLoadCutOffText.setEnabled(true);
        //setMotorLoadThreshold.setEnabled(true);
        setMotorLoadThreshold.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableViews() {
        noLoadCutoffText.setFocusableInTouchMode(false);
        fullLoadCutOffText.setFocusableInTouchMode(false);

        //disable views
        noLoadCutoffText.setEnabled(false);
        fullLoadCutOffText.setEnabled(false);
        //setMotorLoadThreshold.setEnabled(false);
        setMotorLoadThreshold.setVisibility(View.INVISIBLE);
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
             //   System.out.println("At screen_10 randomValue = "+randomValue+" and randomNumber = "+randomNumber+" , randomNumber vs randomValue =  "+(randomNumber == randomValue)+" , screen_10_Visible = "+screen_10_Visible);
                if (b && (randomNumber == randomValue) && screen_10_Visible) {
                    System.out.println("screen_10 b = "+b);
                    systemDown = true;
                    disableViews();
                    smsReceiver.unRegisterBroadCasts();
                    status.setText(SmsUtils.SYSTEM_DOWN);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Screen_10.this, MainActivity_GSM.class));
                            finish();
                        }
                    }, 3000);
                }
            }
        });

        this.setSmsServiceBroadcast(new SmsServiceBroadcast() {
            @Override
            public void onReceiveSmsDeliveredStatus(boolean smsDeliveredStatus) {
                //System.out.println("non service page smsDeliveredStatus - " + smsDeliveredStatus);
                if (smsDeliveredStatus) {
                    smsReceiver.waitFor_1_Minute(randomNumber,smsReceiver);
                    b = true;
                } else {
                    isSetMotorLoadClicked = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
        screen_10_Visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
        screen_10_Visible = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Screen_10.this, Screen_9.class));
        finish();
    }

    public void checkSMS(String message) {
        if (message.toLowerCase().contains(SmsUtils.INSMS_12_1.toLowerCase()) && isSetMotorLoadClicked) {
            b = false;
            enableViews();
            isSetMotorLoadClicked = false;
            status.setText("Motorload thresholds set successfully.");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(new Intent(Screen_10.this, Screen_9.class));
                }
            }, 1000);
        }
    }
}
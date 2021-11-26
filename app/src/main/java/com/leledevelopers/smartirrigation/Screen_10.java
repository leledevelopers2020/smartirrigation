package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
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
    private SmsUtils smsUtils=new SmsUtils();
    private Boolean b,systemDown = false;
    EditText noLoadCutoffText, fullLoadCutOffText;
    private Button setMotorLoadThreshold,back_10;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen10);
        initViews();
        setMotorLoadThreshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if(validateInput(noLoadCutoffText.getText().toString(),fullLoadCutOffText.getText().toString()) && !systemDown)
                {
                    cursorVisibility();
                    String smsData=smsUtils.OutSMS_12(noLoadCutoffText.getText().toString(),
                            fullLoadCutOffText.getText().toString());
                    sendMessage(SmsServices.phoneNumber,smsData);
                    smsReceiver.waitFor_1_Minute();
                    b = true;
                    status.setText("Message Delivered");
                }
            }
        });
        back_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                startActivity(new Intent(Screen_10.this,Screen_9.class));
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
                if(!hasFocus)
                {
                    if(noLoadCutoffText.getText().toString().length()==0 &&
                            validateRange(0,1024,Integer.parseInt(noLoadCutoffText.getText().toString())) )
                    {
                        noLoadCutoffText.getText().clear();
                        noLoadCutoffText.setError("Please enter the data");

                    }
                }
            }
        });
        fullLoadCutOffText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {

                    if(fullLoadCutOffText.getText().toString().length()==0 &&
                            validateRange(0,1024,Integer.parseInt(fullLoadCutOffText.getText().toString())))
                    {

                        fullLoadCutOffText.getText().clear();
                        fullLoadCutOffText.setError("Please enter the data");
                    }
                }
            }
        });
        fullLoadCutOffText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE)
                {

                    try {

                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    fullLoadCutOffText.clearFocus();
                }
                return true;
            }
        });
    }



    private boolean validateInput(String noLoadCutoffTextlocal, String fullLoadCutOffTextlocal) {
        Boolean validate=true;
        if(noLoadCutoffText.getText().toString().length()==0 &&
                validateRange(0,1024,Integer.parseInt(noLoadCutoffText.getText().toString())) )
        {
            noLoadCutoffText.getText().clear();
            noLoadCutoffText.setError("Please enter the data");
            return false;
        }
        if(fullLoadCutOffText.getText().toString().length()==0 &&
                validateRange(0,1024,Integer.parseInt(fullLoadCutOffText.getText().toString())))
        {

            fullLoadCutOffText.getText().clear();
            fullLoadCutOffText.setError("Please enter the data");
            return false;
        }
        return validate;
    }
    private boolean validateRange(int min, int max, int inputValue) {
        if(inputValue>=min && inputValue <=max)
        {
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
        back_10=findViewById(R.id.back_10);
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
                if (SmsServices.phoneNumber.replaceAll("\\s", "").equals(phoneNumber.replaceAll("\\s", "")) && !systemDown) {
                    checkSMS(message);
                } else if (phoneNumber.contains(SmsServices.phoneNumber.replaceAll("\\s", "")) && !systemDown) {
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
        startActivity(new Intent(Screen_10.this,Screen_9.class));
        finish();
    }

    public void checkSMS(String message) {
        switch (message) {
            case SmsUtils.INSMS_12_1: {
                status.setText("MOTOR Load Threshold is Set");
                break;
            }
        }
    }

    
}
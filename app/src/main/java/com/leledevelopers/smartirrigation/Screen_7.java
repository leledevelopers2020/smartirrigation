package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.leledevelopers.smartirrigation.models.ConfigureFieldIrrigationModel;
import com.leledevelopers.smartirrigation.models.FiltrationModel;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.IOException;

public class Screen_7 extends SmsServices {
    private static final String TAG = Screen_7.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b, systemDown = false;
    EditText filtrationControlUnitNoDelay_1, filtrationControlUnitNoDelay_2, filtrationControlUnitNoDelay_3;
    EditText filtrationControlUnitOnTime, filtrationControlUnitSeparation;
    private Button enableFiltration, disableFiltration, back_7;
    private TextView status;
    private FiltrationModel model;
    private CURD_Files curd_files = new CURD_FilesImpl();
    private SmsUtils smsUtils = new SmsUtils();
    private String regex = "\\d+";
    private boolean isEditedDelay_1 = false;
    private boolean isEditedDelay_2 = false;
    private boolean isEditedDelay_3 = false;
    private boolean isEditedOnTime = false;
    private boolean isEditedSeparation = false;
    private boolean isInitial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen7);
        initViews();
        initializeModel();
        filtrationControlUnitNoDelay_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrationControlUnitNoDelay_1.setCursorVisible(true);
                System.out.println("--->++---> " + filtrationControlUnitNoDelay_1.getText().toString() + " & " + model.getFcDelay_1());
            }
        });
        filtrationControlUnitNoDelay_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrationControlUnitNoDelay_2.setCursorVisible(true);
                System.out.println("--->++---> " + filtrationControlUnitNoDelay_2.getText().toString() + " & " + model.getFcDelay_2());
            }
        });
        filtrationControlUnitNoDelay_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrationControlUnitNoDelay_3.setCursorVisible(true);
                System.out.println("--->++---> " + filtrationControlUnitNoDelay_3.getText().toString() + " & " + model.getFcDelay_3());
            }
        });
        filtrationControlUnitOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrationControlUnitOnTime.setCursorVisible(true);
                System.out.println("--->++---> " + filtrationControlUnitOnTime.getText().toString() + " & " + model.getFcOnTime());
            }
        });
        filtrationControlUnitSeparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrationControlUnitSeparation.setCursorVisible(true);
                System.out.println("--->++---> " + filtrationControlUnitSeparation.getText().toString() + " & " + model.getFcSeperation());
            }
        });
        enableFiltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                cursorVisibility();
                if (validateInput() && !systemDown) {
                    disableEditText();
                    updateData_And_SendSMS("enable");
                    smsReceiver.waitFor_1_Minute();
                    b = true;
                }
            }
        });
        disableFiltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (!systemDown) {
                    disableEditText();
                    updateData_And_SendSMS("disable");
                    smsReceiver.waitFor_1_Minute();
                    b = false;
                    status.setText("Disabled");
                }
            }
        });
        back_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                startActivity(new Intent(Screen_7.this, Screen_4.class));
                finish();
            }
        });
        filtrationControlUnitNoDelay_1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(filtrationControlUnitNoDelay_1.getText().toString().matches(regex) &&
                            filtrationControlUnitNoDelay_1.getText().toString().length() >= 1
                            && validateRange(1, 60, Integer.parseInt(filtrationControlUnitNoDelay_1.getText().toString())))) {

                        filtrationControlUnitNoDelay_1.getText().clear();
                        filtrationControlUnitNoDelay_1.setError("Enter a valid value");
                    }
                    if (isInitial) {
                        disableFiltration.setVisibility(View.INVISIBLE);
                    } else if (filtrationControlUnitNoDelay_1.getText().toString().equals(model.getFcDelay_1() + "")) {
                        System.out.println("--->++---> " + "yes");
                        isEditedDelay_1 = false;
                        isAnyViewEdited();
                    } else {
                        System.out.println("--->++---> " + "No");
                        isEditedDelay_1 = true;
                        isAnyViewEdited();
                    }
                }
            }
        });
        filtrationControlUnitNoDelay_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(filtrationControlUnitNoDelay_2.getText().toString().matches(regex) &&
                            filtrationControlUnitNoDelay_2.getText().toString().length() >= 1
                            && validateRange(1, 10, Integer.parseInt(filtrationControlUnitNoDelay_2.getText().toString())))) {

                        filtrationControlUnitNoDelay_2.getText().clear();
                        filtrationControlUnitNoDelay_2.setError("Enter a valid value");

                    }
                    if (isInitial) {
                        disableFiltration.setVisibility(View.INVISIBLE);
                    } else if (filtrationControlUnitNoDelay_2.getText().toString().equals(model.getFcDelay_2() + "")) {
                        System.out.println("--->++---> " + "yes");
                        isEditedDelay_2 = false;
                        isAnyViewEdited();
                    } else {
                        System.out.println("--->++---> " + "No");
                        isEditedDelay_2 = true;
                        isAnyViewEdited();
                    }
                }
            }
        });
        filtrationControlUnitNoDelay_3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!(filtrationControlUnitNoDelay_3.getText().toString().matches(regex) &&
                            filtrationControlUnitNoDelay_3.getText().toString().length() >= 1
                            && validateRange(1, 10, Integer.parseInt(filtrationControlUnitNoDelay_3.getText().toString())))) {

                        filtrationControlUnitNoDelay_3.getText().clear();
                        filtrationControlUnitNoDelay_3.setError("Enter a valid value");

                    }
                    if (isInitial) {
                        disableFiltration.setVisibility(View.INVISIBLE);
                    } else if (filtrationControlUnitNoDelay_3.getText().toString().equals(model.getFcDelay_3() + "")) {
                        System.out.println("--->++---> " + "yes");
                        isEditedDelay_3 = false;
                        isAnyViewEdited();
                    } else {
                        System.out.println("--->++---> " + "No");
                        isEditedDelay_3 = true;
                        isAnyViewEdited();
                    }
                }
            }
        });
        filtrationControlUnitOnTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(filtrationControlUnitOnTime.getText().toString().matches(regex) &&
                            filtrationControlUnitOnTime.getText().toString().length() >= 1
                            && validateRange(1, 10, Integer.parseInt(filtrationControlUnitOnTime.getText().toString())))) {
                        filtrationControlUnitOnTime.getText().clear();
                        filtrationControlUnitOnTime.setError("Enter a valid value");
                    }
                    if (isInitial) {
                        disableFiltration.setVisibility(View.INVISIBLE);
                    } else if (filtrationControlUnitOnTime.getText().toString().equals(model.getFcOnTime() + "")) {
                        System.out.println("--->++---> " + "yes");
                        isEditedOnTime = false;
                        isAnyViewEdited();
                    } else {
                        System.out.println("--->++---> " + "No");
                        isEditedOnTime = true;
                        isAnyViewEdited();
                    }
                }
            }
        });
        filtrationControlUnitSeparation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    if (!(filtrationControlUnitSeparation.getText().toString().matches(regex) &&
                            filtrationControlUnitSeparation.getText().toString().length() >= 2
                            && validateRange(10, 240, Integer.parseInt(filtrationControlUnitSeparation.getText().toString())))) {
                        filtrationControlUnitSeparation.requestFocus();
                        filtrationControlUnitSeparation.getText().clear();
                        filtrationControlUnitSeparation.setError("Enter a valid value");
                    }
                    if (isInitial) {
                        disableFiltration.setVisibility(View.INVISIBLE);
                    } else if (filtrationControlUnitSeparation.getText().toString().equals(model.getFcSeperation() + "")) {
                        System.out.println("--->++---> " + "yes");
                        isEditedSeparation = false;
                        isAnyViewEdited();
                    } else {
                        System.out.println("--->++---> " + "No");
                        isEditedSeparation = true;
                        isAnyViewEdited();
                    }
                }
            }
        });
        filtrationControlUnitSeparation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                    filtrationControlUnitSeparation.clearFocus();

                }
                return true;
            }
        });

    }

    @Override
    public void initViews() {
        filtrationControlUnitNoDelay_1 = findViewById(R.id.filtrationControlUnitNoDelay_1);
        filtrationControlUnitNoDelay_2 = findViewById(R.id.filtrationControlUnitNoDelay_2);
        filtrationControlUnitNoDelay_3 = findViewById(R.id.filtrationControlUnitNoDelay_3);
        filtrationControlUnitOnTime = findViewById(R.id.filtrationControlUnitOnTime);
        filtrationControlUnitSeparation = findViewById(R.id.filtrationControlUnitSeparation);
        enableFiltration = findViewById(R.id.enableFiltration7);
        disableFiltration = findViewById(R.id.disableFiltration7);
        back_7 = findViewById(R.id.back_7);
        status = findViewById(R.id.screen_7_status);
    }
    private void disableEditText() {
        filtrationControlUnitNoDelay_1.setFocusableInTouchMode(false);
        filtrationControlUnitNoDelay_2.setFocusableInTouchMode(false);
        filtrationControlUnitNoDelay_3.setFocusableInTouchMode(false);
        filtrationControlUnitOnTime.setFocusableInTouchMode(false);
        filtrationControlUnitSeparation.setFocusableInTouchMode(false);
    }
    private void enableEditText() {
        filtrationControlUnitNoDelay_1.setFocusableInTouchMode(true);
        filtrationControlUnitNoDelay_2.setFocusableInTouchMode(true);
        filtrationControlUnitNoDelay_3.setFocusableInTouchMode(true);
        filtrationControlUnitOnTime.setFocusableInTouchMode(true);
        filtrationControlUnitSeparation.setFocusableInTouchMode(true);
    }


    private void updateData_And_SendSMS(String typeOfAction) {
        String smsData;
        if (typeOfAction.equals("enable")) {
            model.setFcDelay_1(Integer.parseInt(filtrationControlUnitNoDelay_1.getText().toString()));
            model.setFcDelay_2(Integer.parseInt(filtrationControlUnitNoDelay_2.getText().toString()));
            model.setFcDelay_3(Integer.parseInt(filtrationControlUnitNoDelay_3.getText().toString()));
            model.setFcOnTime(Integer.parseInt(filtrationControlUnitOnTime.getText().toString()));
            model.setFcSeperation(Integer.parseInt(filtrationControlUnitSeparation.getText().toString()));
            model.setEnabled(true);
            System.out.println("after set " + model.toString());
            smsData = smsUtils.OutSMS_8(model.getFcDelay_1() + "", model.getFcDelay_2() + ""
                    , model.getFcDelay_3() + "", model.getFcOnTime() + "",
                    model.getFcSeperation() + "");
            enableFiltration.setVisibility(View.INVISIBLE);
            disableFiltration.setVisibility(View.INVISIBLE);
        } else {
            smsData = smsUtils.OutSMS_9;
            enableFiltration.setVisibility(View.VISIBLE);
            disableFiltration.setVisibility(View.INVISIBLE);
        }
        sendMessage(SmsServices.phoneNumber, smsData);
        isEditedDelay_1 = false;
        isEditedDelay_2 = false;
        isEditedDelay_3 = false;
        isEditedSeparation = false;
        isEditedOnTime = false;
    }

    private void isAnyViewEdited() {
        if (isEditedDelay_1 || isEditedDelay_2 || isEditedDelay_3 || isEditedOnTime || isEditedSeparation) {
            enableFiltration.setVisibility(View.VISIBLE);
            disableFiltration.setVisibility(View.INVISIBLE);
        } else {
            disableFiltration.setVisibility(View.VISIBLE);
            enableFiltration.setVisibility(View.INVISIBLE);
        }
        // return (isEditedDelay_1 || isEditedDelay_2 || isEditedDelay_3 || isEditedOnTime || isEditedSeparation) ? true : false;
    }

    private boolean validateInput() {
        if (!(filtrationControlUnitNoDelay_1.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_1.getText().toString().length() >= 1
                && validateRange(1, 60, Integer.parseInt(filtrationControlUnitNoDelay_1.getText().toString())))) {

            filtrationControlUnitNoDelay_1.getText().clear();
            filtrationControlUnitNoDelay_1.setError("Enter a valid value");
            return false;
        }
        if (!(filtrationControlUnitNoDelay_2.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_2.getText().toString().length() >= 1
                && validateRange(1, 10, Integer.parseInt(filtrationControlUnitNoDelay_2.getText().toString())))) {

            filtrationControlUnitNoDelay_2.getText().clear();
            filtrationControlUnitNoDelay_2.setError("Enter a valid value");
            return false;

        }
        if (!(filtrationControlUnitNoDelay_3.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_3.getText().toString().length() >= 1
                && validateRange(1, 10, Integer.parseInt(filtrationControlUnitNoDelay_3.getText().toString())))) {

            filtrationControlUnitNoDelay_3.getText().clear();
            filtrationControlUnitNoDelay_3.setError("Enter a valid value");
            return false;

        }
        if (!(filtrationControlUnitOnTime.getText().toString().matches(regex) &&
                filtrationControlUnitOnTime.getText().toString().length() >= 1
                && validateRange(1, 10, Integer.parseInt(filtrationControlUnitOnTime.getText().toString())))) {
            filtrationControlUnitOnTime.getText().clear();
            filtrationControlUnitOnTime.setError("Enter a valid value");
            return false;
        }

        if (!(filtrationControlUnitSeparation.getText().toString().matches(regex) &&
                filtrationControlUnitSeparation.getText().toString().length() >= 2
                && validateRange(10, 240, Integer.parseInt(filtrationControlUnitSeparation.getText().toString())))) {
            filtrationControlUnitSeparation.requestFocus();
            filtrationControlUnitSeparation.getText().clear();
            filtrationControlUnitSeparation.setError("Enter a valid value");
            return false;
        }

        return true;
    }

    private boolean validateRange(int min, int max, int inputValue) {
        Log.d("tag", "Validate range min : " + min + " validate range max : " + max + " validate range input val : " + inputValue);

        if (inputValue >= min && inputValue <= max) {
            return true;
        }
        return false;
    }


    private void initializeModel() {
        if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_FILTRATION_FILE)) {
            try {
                isInitial = false;
                model = (FiltrationModel) curd_files.getFile(getApplicationContext(), ProjectUtils.CONFG_FILTRATION_FILE);
                System.out.println("getting file " + model.toString());
                if (model.isEnabled()) {
                    System.out.println("isEnabled " + model.isEnabled());
                    filtrationControlUnitNoDelay_1.setText(model.getFcDelay_1() + "");
                    filtrationControlUnitNoDelay_2.setText(model.getFcDelay_2() + "");
                    filtrationControlUnitNoDelay_3.setText(model.getFcDelay_3() + "");
                    filtrationControlUnitOnTime.setText(model.getFcOnTime() + "");
                    filtrationControlUnitSeparation.setText(model.getFcSeperation() + "");
                //    disableFiltration.setVisibility(View.VISIBLE);
                    enableFiltration.setVisibility(View.INVISIBLE);
                } else {
                    System.out.println("isEnabled " + model.isEnabled());
                    filtrationControlUnitNoDelay_1.setText("");
                    filtrationControlUnitNoDelay_2.setText("");
                    filtrationControlUnitNoDelay_3.setText("");
                    filtrationControlUnitOnTime.setText("");
                    filtrationControlUnitSeparation.setText("");
                    disableFiltration.setVisibility(View.INVISIBLE);
                  //  enableFiltration.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(Screen_7.this, "NO data", Toast.LENGTH_LONG).show();
            model = new FiltrationModel();
            isInitial = true;
            disableFiltration.setVisibility(View.INVISIBLE);
        }
    }

    private void cursorVisibility() {

        try {
            filtrationControlUnitNoDelay_1.setCursorVisible(false);
            filtrationControlUnitNoDelay_2.setCursorVisible(false);
            filtrationControlUnitNoDelay_3.setCursorVisible(false);
            filtrationControlUnitOnTime.setCursorVisible(false);
            filtrationControlUnitSeparation.setCursorVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
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
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Screen_7.this,MainActivity_GSM.class));
                            finish();
                        }
                    },2000);
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
        startActivity(new Intent(Screen_7.this, Screen_4.class));
        finish();
    }

    public void checkSMS(String message) {
        enableEditText();
        switch (message) {
            case SmsUtils.INSMS_8_1: {
                status.setText("Pump Filtration Activated");
                try {
                    System.out.println("pushing to file " + model.toString());
                    curd_files.updateFile(getApplicationContext(), ProjectUtils.CONFG_FILTRATION_FILE, model);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case SmsUtils.INSMS_9_1: {
                status.setText("Pump Filtration De-Activated");
                break;
            }
        }
        initializeModel();
    }
}
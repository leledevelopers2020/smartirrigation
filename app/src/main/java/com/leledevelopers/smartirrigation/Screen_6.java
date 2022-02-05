package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.leledevelopers.smartirrigation.models.BaseConfigurationFeildFertigationModel;
import com.leledevelopers.smartirrigation.models.ConfigurationFeildFertigationModel;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.SmsTesting;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Screen_6 extends SmsServices {
    private static final String TAG = Screen_6.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b=false, systemDown = false;
    private Spinner spinner;
    EditText wetPeriod, injectPeriod, noOfIterations;
    private Button enableFieldFertigation, disableFieldFertigation, back_6;
    private ArrayAdapter<CharSequence> adapter;
    private TextView status;
    private ConfigurationFeildFertigationModel model;
    private CURD_Files<ConfigurationFeildFertigationModel> curd_files = new CURD_FilesImpl<ConfigurationFeildFertigationModel>();
    private List<ConfigurationFeildFertigationModel> modelList = new ArrayList<ConfigurationFeildFertigationModel>();
    private BaseConfigurationFeildFertigationModel baseConfigurationFeildFertigationModel = new BaseConfigurationFeildFertigationModel();
    private SmsUtils smsUtils = new SmsUtils();
    private String regex = "\\d+";
    private int fieldNo;
    private boolean isEditedInjectPeriod = false;
    private boolean isEditedNoOfIterations = false;
    private boolean isEditedWetPeriod = false;
    private boolean isInitial = false;
    private boolean isEnabledClicked = false;
    private boolean isDisabledClicked = false;
    private boolean handlerActivated=false;
    private double randomNumber=-1;
    private static boolean screen_6_Visible = false;
    private  SmsTesting smsTesting=new SmsTesting();
    private  StringBuffer activityMessage=new StringBuffer("");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen6);
        this.context = getApplicationContext();
        initViews();
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        initializeModel();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (modelList.get(position).isEnabled() || !modelList.get(position).isModelEmpty()) {
                    model = modelList.get(position);
                    wetPeriod.setText(model.getWetPeriod() + "");
                    injectPeriod.setText(model.getInjectPeriod() + "");
                    noOfIterations.setText(model.getNoIterations() + "");

                    if (model.isEnabled()) {
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                        enableFieldFertigation.setVisibility(View.INVISIBLE);
                    } else {
                        disableFieldFertigation.setVisibility(View.INVISIBLE);
                        enableFieldFertigation.setVisibility(View.VISIBLE);
                    }
                    isInitial = false;
                } else {
                    isInitial = true;
                    setEmptyData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        wetPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wetPeriod.setCursorVisible(true);
            }
        });

        injectPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                injectPeriod.setCursorVisible(true);
            }
        });

        noOfIterations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noOfIterations.setCursorVisible(true);
            }
        });

        enableFieldFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (validateInput() && !systemDown) {
                    status.setText("Enable fertigation configuration SMS sent");
                    disableViews();
                    cursorVisibility();
                    activityMessage.replace(0,activityMessage.length(),"Enable fertigation configuration SMS ");
                    updateData_And_SendSMS("enable","Enable fertigation configuration SMS ");
                    isEnabledClicked = true;
                }
            }
        });

        disableFieldFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (!systemDown) {
                    status.setText("Disable fertigation configuration SMS sent");
                    disableViews();
                    activityMessage.replace(0,activityMessage.length(),"Disable fertigation configuration SMS ");
                    updateData_And_SendSMS("disable","Disable fertigation configuration SMS ");

                    randomNumber = Math.random();
                    isDisabledClicked = true;
                }
            }
        });
        back_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomNumber=-1;
                Intent intentB=(new Intent(Screen_6.this, Screen_4.class));
                intentB.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentB);

            }
        });
        wetPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(wetPeriod.getText().toString().matches(regex)
                            && wetPeriod.getText().toString().length() >= 1
                            && validateRange(1, 999, Integer.parseInt(wetPeriod.getText().toString())))) {
                        wetPeriod.getText().clear();
                        wetPeriod.setError("Enter a valid value");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.INVISIBLE);
                    } else if (model.isEnabled()) {
                        if (wetPeriod.getText().toString().equals(model.getWetPeriod() + "")) {
                            isEditedWetPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedWetPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        injectPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(injectPeriod.getText().toString().matches(regex)
                            && injectPeriod.getText().toString().length() >= 1
                            && validateRange(1, 999, Integer.parseInt(injectPeriod.getText().toString())))) {
                        injectPeriod.getText().clear();
                        injectPeriod.setError("Enter a valid value");

                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.INVISIBLE);
                    } else if (model.isEnabled()) {
                        if (injectPeriod.getText().toString().equals(model.getInjectPeriod() + "")) {
                            isEditedInjectPeriod = false;
                            isAnyViewEdited();
                        } else {
                            isEditedInjectPeriod = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        noOfIterations.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!(noOfIterations.getText().toString().matches(regex)
                            && noOfIterations.getText().toString().length() == 1
                            && validateRange(1, 5, Integer.parseInt(noOfIterations.getText().toString())))) {
                        noOfIterations.getText().clear();
                        noOfIterations.setError("Enter a valid value");
                    }
                    if (isInitial) {
                        disableFieldFertigation.setVisibility(View.INVISIBLE);
                    } else if (model.isEnabled()) {
                        if (noOfIterations.getText().toString().equals(model.getNoIterations() + "")) {
                            isEditedNoOfIterations = false;
                            isAnyViewEdited();
                        } else {
                            isEditedNoOfIterations = true;
                            isAnyViewEdited();
                        }
                    }
                }
            }
        });
        noOfIterations.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    try {

                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    noOfIterations.clearFocus();
                }
                return true;
            }
        });
    }

    @Override
    public void initViews() {
        spinner = (Spinner) findViewById(R.id.fieldNoSpinner6);
        wetPeriod = findViewById(R.id.wetPeriod);
        injectPeriod = findViewById(R.id.injectPeriod);
        noOfIterations = findViewById(R.id.noOfIterations);
        enableFieldFertigation = findViewById(R.id.enableFieldFertigation6);
        disableFieldFertigation = findViewById(R.id.disableFieldFertigation6);
        back_6 = findViewById(R.id.back_6);
        status = findViewById(R.id.screen_6_status);
    }

    @Override
    public void enableViews() {
        spinner.setEnabled(true);
        wetPeriod.setEnabled(true);
        injectPeriod.setEnabled(true);
        noOfIterations.setEnabled(true);

    }

    @Override
    public void disableViews() {
        spinner.setEnabled(false);
        wetPeriod.setEnabled(false);
        injectPeriod.setEnabled(false);
        noOfIterations.setEnabled(false);
    }


    private void isAnyViewEdited() {
        if (isEditedNoOfIterations || isEditedInjectPeriod || isEditedWetPeriod) {
            enableFieldFertigation.setVisibility(View.VISIBLE);
            disableFieldFertigation.setVisibility(View.INVISIBLE);
        } else {
            disableFieldFertigation.setVisibility(View.VISIBLE);
            enableFieldFertigation.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public String toString() {
        return "Screen_6{" +
                "isEditedInjectPeriod=" + isEditedInjectPeriod +
                ", isEditedNoOfIterations=" + isEditedNoOfIterations +
                ", isEditedWetPeriod=" + isEditedWetPeriod +
                ", isInitial=" + isInitial +
                '}';
    }

    private boolean validateInput() {
        if (spinner.getSelectedItem().toString().trim().equals("Pick one")) {

            return false;
        }
        if (!(wetPeriod.getText().toString().matches(regex)
                && wetPeriod.getText().toString().length() >= 1
                && validateRange(1, 999, Integer.parseInt(wetPeriod.getText().toString())))) {
            wetPeriod.getText().clear();
            wetPeriod.setError("Enter a valid value");
            return false;
        }
        if (!(injectPeriod.getText().toString().matches(regex)
                && injectPeriod.getText().toString().length() >= 1
                && validateRange(1, 999, Integer.parseInt(injectPeriod.getText().toString())))) {
            injectPeriod.getText().clear();
            injectPeriod.setError("Enter a valid value");
            return false;
        }
        if (!(noOfIterations.getText().toString().matches(regex)
                && noOfIterations.getText().toString().length()==1
                &&validateRange(1, 5, Integer.parseInt(noOfIterations.getText().toString())))) {
            noOfIterations.getText().clear();
            noOfIterations.setError("Enter a valid value");
            return false;
        }


        return true;
    }

    private boolean validateRange(int min, int max, int inputValue) {
        if (inputValue >= min && inputValue <= max) {
            return true;
        }
        return false;
    }

    private void initializeModel() {
        try {
            if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_FERTIGATION_FILE)) {
                 baseConfigurationFeildFertigationModel = (BaseConfigurationFeildFertigationModel) curd_files.getFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE);
                modelList = baseConfigurationFeildFertigationModel.getModelList();
                if (baseConfigurationFeildFertigationModel.getLastEnabledFieldNo() != -1) {
                    model = modelList.get(baseConfigurationFeildFertigationModel.getLastEnabledFieldNo());
                    if (model.isEnabled() || !model.isModelEmpty()) {
                        spinner.setSelection(model.getFieldNo() - 1);
                        wetPeriod.setText(model.getWetPeriod() + "");
                        injectPeriod.setText(model.getInjectPeriod() + "");
                        noOfIterations.setText(model.getNoIterations() + "");

                        if (model.isEnabled()) {
                            disableFieldFertigation.setVisibility(View.VISIBLE);
                            enableFieldFertigation.setVisibility(View.INVISIBLE);
                        } else {
                            disableFieldFertigation.setVisibility(View.INVISIBLE);
                            enableFieldFertigation.setVisibility(View.VISIBLE);
                        }
                    } else {
                        isInitial = true;
                        disableFieldFertigation.setVisibility(View.INVISIBLE);
                        enableFieldFertigation.setVisibility(View.VISIBLE);
                    }
                } else {
                    isInitial = true;
                    disableFieldFertigation.setVisibility(View.INVISIBLE);
                    enableFieldFertigation.setVisibility(View.VISIBLE);
                }
            } else {
                for (int i = 0; i < 12; i++) {
                    ConfigurationFeildFertigationModel modelData = new ConfigurationFeildFertigationModel();
                    modelData.setFieldNo(i);
                    modelData.setEnabled(false);
                    modelList.add(modelData);
                    baseConfigurationFeildFertigationModel.setModelList(modelList);
                }
                curd_files.createFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE, baseConfigurationFeildFertigationModel);
                isInitial = true;
                setEmptyData();
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setEmptyData() {
        disableFieldFertigation.setVisibility(View.INVISIBLE);
        enableFieldFertigation.setVisibility(View.VISIBLE);
        wetPeriod.setText("");
        injectPeriod.setText("");
        noOfIterations.setText("");
        model = null;
    }

    private void updateData_And_SendSMS(String typeOfAction,String screen_Specific_SMS) {
        if (!spinner.getSelectedItem().toString().trim().equals("Pick one")) {
            String smsdata;
            fieldNo = Integer.parseInt(spinner.getSelectedItem().toString());
            model = modelList.get(fieldNo - 1);
            model.setFieldNo(Integer.parseInt(spinner.getSelectedItem().toString()));
            model.setWetPeriod(Integer.parseInt(wetPeriod.getText().toString()));
            model.setInjectPeriod(Integer.parseInt(injectPeriod.getText().toString()));
            model.setNoIterations(Integer.parseInt(noOfIterations.getText().toString()));
            if (typeOfAction.equals("enable")) {
                model.setEnabled(true);
                model.setModelEmpty(false);
                smsdata = smsUtils.OutSMS_6((model.getFieldNo() < 10 ? String.format("%02d", model.getFieldNo()) : model.getFieldNo() + ""), model.getWetPeriod(),
                        model.getInjectPeriod(), model.getNoIterations());
                baseConfigurationFeildFertigationModel.setLastEnabledFieldNo(fieldNo - 1);
                enableFieldFertigation.setVisibility(View.INVISIBLE);
                disableFieldFertigation.setVisibility(View.INVISIBLE);
                isInitial = false;
            } else {
                model.setEnabled(false);
                model.setModelEmpty(false);
                smsdata = smsUtils.OutSMS_7((fieldNo < 10 ? String.format("%02d", fieldNo) : fieldNo + ""));
                baseConfigurationFeildFertigationModel.setLastEnabledFieldNo(fieldNo - 1);
                disableFieldFertigation.setVisibility(View.INVISIBLE);
            }
            randomNumber = Math.random();
            SmsTesting.contextPri=getApplicationContext();
            smsTesting.sendMessageBox(SmsServices.phoneNumber, smsdata,   randomNumber,screen_Specific_SMS);
            System.out.println("screen 6 "+randomNumber);
            modelList.set(fieldNo - 1, model);
            isEditedInjectPeriod = false;
            isEditedNoOfIterations = false;
            isEditedWetPeriod = false;
        } else {
            Toast.makeText(Screen_6.this, "Please select the field no", Toast.LENGTH_LONG).show();
            enableFieldFertigation.setVisibility(View.VISIBLE);
        }
    }

    private void cursorVisibility() {
        try {
            wetPeriod.setCursorVisible(false);
            injectPeriod.setCursorVisible(false);
            noOfIterations.setCursorVisible(false);
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
                if (SmsServices.phoneNumber.replaceAll("\\s", "").equals(phoneNumber.replaceAll("\\s", "")) && !systemDown) {
                    checkSMS(message);

                } else if (phoneNumber.contains(SmsServices.phoneNumber.replaceAll("\\s", "")) && !systemDown) {
                    checkSMS(message);
                }

            }

            @Override
            public void checkTime(double randomValue) {
                System.out.println("At screen_6 randomValue = "+randomValue+" and randomNumber = "+randomNumber+" , randomNumber vs randomValue =  "+(randomNumber == randomValue)+" , screen_6_Visible = "+screen_6_Visible);
                if (b && (randomNumber == randomValue) && screen_6_Visible) {
                    disableViews();
                    systemDown = true;
                    smsReceiver.unRegisterBroadCasts();
                    status.setText(SmsUtils.SYSTEM_DOWN);
                    handlerActivated=false;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            randomNumber=-1;
                            Intent intentS=(new Intent(Screen_6.this, MainActivity_GSM.class));
                            intentS.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentS);
                        }
                    }, 5000);
                }
            }

        });

        smsTesting.setSmsServiceBroadcast(new SmsServiceBroadcast() {
            @Override
            public void onReceiveSmsDeliveredStatus(boolean smsDeliveredStatus,String message) {
                if (smsDeliveredStatus) {
                    if(message.equals(activityMessage.toString()) && !(handlerActivated) ) {
                            handlerActivated=true;
                         //   status.setText(message + " sent");
                            smsReceiver.waitFor_1_Minute(randomNumber, smsReceiver);
                            b = true;
                        }
                } else {
                    status.setText(message+" sending failed");
                    isEnabledClicked = false;
                    isDisabledClicked = false;
                    enableViews();
                    initializeModel();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsReceiver.registerBroadCasts();
        screen_6_Visible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        smsReceiver.unRegisterBroadCasts();
        screen_6_Visible = false;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        randomNumber=-1;
        startActivity(new Intent(Screen_6.this, Screen_4.class));
        finish();
    }

    public void checkSMS(String message) {
        try {
            if (message.toLowerCase().contains(SmsUtils.INSMS_6_1.toLowerCase()) && isEnabledClicked) {
                if (Integer.parseInt(message.substring(SmsUtils.INSMS_6_1.length()).trim()) == model.getFieldNo()) {
                    b = false;
                    handlerActivated=false;
                    isEnabledClicked = false;
                    baseConfigurationFeildFertigationModel.setModelList(modelList);
                    curd_files.updateFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE, baseConfigurationFeildFertigationModel);
                    status.setText("Fertigation enabled for field no. " + model.getFieldNo());
                    enableViews();
                    initializeModel();
                }
            } else if (message.toLowerCase().contains(SmsUtils.INSMS_6_2.toLowerCase())) {
                b = false;
                handlerActivated=false;
                status.setText("Incorrect values Fertigation not enabled for field no." + model.getFieldNo());
                enableViews();
                initializeModel();
            }
            else if(message.toLowerCase().contains(SmsUtils.INSMS_6_3.toLowerCase()))
            {
                b=false;
                handlerActivated=false;
                status.setText(message+model.getFieldNo());
                enableViews();
                initializeModel();
            }
            else if (message.toLowerCase().contains(SmsUtils.INSMS_7_1.toLowerCase()) && isDisabledClicked) {
                if (Integer.parseInt(message.substring(SmsUtils.INSMS_7_1.length()).trim()) == model.getFieldNo()) {
                    b = false;
                    isDisabledClicked = false;
                    handlerActivated=false;
                    baseConfigurationFeildFertigationModel.setModelList(modelList);
                    curd_files.updateFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE, baseConfigurationFeildFertigationModel);
                    status.setText("Fertigation disabled for field no." + model.getFieldNo());
                    enableViews();
                    initializeModel();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

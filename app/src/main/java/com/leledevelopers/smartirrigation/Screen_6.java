package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.leledevelopers.smartirrigation.models.BaseConfigureFieldIrrigationModel;
import com.leledevelopers.smartirrigation.models.ConfigurationFeildFertigationModel;
import com.leledevelopers.smartirrigation.models.ConfigureFieldIrrigationModel;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Screen_6 extends SmsServices {
    private static final String TAG = Screen_6.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b, systemDown = false;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen6);
        initViews();
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        initializeModel();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (modelList.get(position).isEnabled()) {
                    model = modelList.get(position);
                    System.out.println(model.toString());
                    wetPeriod.setText(model.getWetPeriod() + "");
                    injectPeriod.setText(model.getInjectPeriod() + "");
                    noOfIterations.setText(model.getNoIterations() + "");
                    disableFieldFertigation.setVisibility(View.VISIBLE);
                    enableFieldFertigation.setVisibility(View.INVISIBLE);
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
                if (validateInput() && !systemDown) {
                    disableEditText();
                    cursorVisibility();
                    updateData_And_SendSMS("enable");
                    smsReceiver.waitFor_1_Minute();
                    b = true;
                }
            }
        });

        disableFieldFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!systemDown) {
                    disableEditText();
                    updateData_And_SendSMS("disable");
                    smsReceiver.waitFor_1_Minute();
                    b = false;
                    status.setText("Field No " + fieldNo + " was disabled");
                }
            }
        });
        back_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_6.this, Screen_4.class));
                finish();
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
                    } else if (wetPeriod.getText().toString().equals(model.getWetPeriod() + "")) {
                        System.out.println();
                        isEditedWetPeriod = false;
                        isAnyViewEdited();
                    } else {
                        System.out.println("wetPeriod " + model.getWetPeriod());
                        isEditedWetPeriod = true;
                        isAnyViewEdited();
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
                    } else if (injectPeriod.getText().toString().equals(model.getInjectPeriod() + "")) {
                        isEditedInjectPeriod = false;
                        isAnyViewEdited();
                    } else {
                        isEditedInjectPeriod = true;
                        isAnyViewEdited();
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
                    } else if (noOfIterations.getText().toString().equals(model.getNoIterations() + "")) {
                        isEditedNoOfIterations = false;
                        isAnyViewEdited();
                    } else {
                        isEditedNoOfIterations = true;
                        isAnyViewEdited();
                    }
                }
            }
        });
        noOfIterations.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {

                    try {

                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
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
    private void disableEditText() {

        spinner.setFocusableInTouchMode(false);
        wetPeriod.setFocusableInTouchMode(false);
        injectPeriod.setFocusableInTouchMode(false);
        noOfIterations.setFocusableInTouchMode(false);

    }
    private void enableEditText()
    {
        spinner.setFocusableInTouchMode(true);
        wetPeriod.setFocusableInTouchMode(true);
        injectPeriod.setFocusableInTouchMode(true);
        noOfIterations.setFocusableInTouchMode(true);
    }

    private void isAnyViewEdited() {
        if (isEditedNoOfIterations || isEditedInjectPeriod || isEditedWetPeriod) {
            enableFieldFertigation.setVisibility(View.VISIBLE);
            disableFieldFertigation.setVisibility(View.INVISIBLE);
        } else {
            disableFieldFertigation.setVisibility(View.VISIBLE);
            enableFieldFertigation.setVisibility(View.INVISIBLE);
        }
        System.out.println(toString());
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
        if (!validateRange(1, 5, Integer.parseInt(noOfIterations.getText().toString()))) {
            noOfIterations.getText().clear();
            noOfIterations.setError("Enter a valid value");
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
        System.out.println("-->initializeModel");
        try {
            if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_FERTIGATION_FILE)) {
                //modelList = curd_files.getFileData(Screen_5.this, ProjectUtils.CONFG_FERTIGATION_FILE);
                baseConfigurationFeildFertigationModel = (BaseConfigurationFeildFertigationModel) curd_files.getFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE);
                modelList = baseConfigurationFeildFertigationModel.getModelList();
                System.out.println("--> Last enabled number = " + baseConfigurationFeildFertigationModel.getLastEnabledFieldNo());
                if (baseConfigurationFeildFertigationModel.getLastEnabledFieldNo() != -1) {
                    model = modelList.get(baseConfigurationFeildFertigationModel.getLastEnabledFieldNo());
                    Toast.makeText(Screen_6.this, model.toString(), Toast.LENGTH_LONG).show();
                    if (model.isEnabled()) {
                        spinner.setSelection(model.getFieldNo() - 1);
                        wetPeriod.setText(model.getWetPeriod() + "");
                        injectPeriod.setText(model.getInjectPeriod() + "");
                        noOfIterations.setText(model.getNoIterations() + "");
                        disableFieldFertigation.setVisibility(View.VISIBLE);
                        enableFieldFertigation.setVisibility(View.INVISIBLE);
                    } else {
                        isInitial = true;
                        disableFieldFertigation.setVisibility(View.INVISIBLE);
                     //   enableFieldFertigation.setVisibility(View.VISIBLE);
                    }
                } else {
                    isInitial = true;
                    disableFieldFertigation.setVisibility(View.INVISIBLE);
                  //  enableFieldFertigation.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(Screen_6.this, "NO data", Toast.LENGTH_LONG).show();
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

    private void updateData_And_SendSMS(String typeOfAction) {
        if (!spinner.getSelectedItem().toString().trim().equals("Pick one")) {
            String smsdata;
            fieldNo = Integer.parseInt(spinner.getSelectedItem().toString());
            if (typeOfAction.equals("enable")) {
                model = modelList.get(fieldNo - 1);
                model.setFieldNo(Integer.parseInt(spinner.getSelectedItem().toString()));
                model.setWetPeriod(Integer.parseInt(wetPeriod.getText().toString()));
                model.setInjectPeriod(Integer.parseInt(injectPeriod.getText().toString()));
                model.setNoIterations(Integer.parseInt(noOfIterations.getText().toString()));
                model.setEnabled(true);
                System.out.println("after set " + model.toString());
                smsdata = smsUtils.OutSMS_6((model.getFieldNo() < 10 ? String.format("%02d", model.getFieldNo()) : model.getFieldNo() + ""), model.getWetPeriod(),
                        model.getInjectPeriod(), model.getNoIterations());
                System.out.println("fieldNo = " + fieldNo);
                baseConfigurationFeildFertigationModel.setLastEnabledFieldNo(fieldNo - 1);
                enableFieldFertigation.setVisibility(View.INVISIBLE);
                disableFieldFertigation.setVisibility(View.INVISIBLE);
                isInitial = false;
            } else {
                smsdata = smsUtils.OutSMS_7((fieldNo < 10 ? String.format("%02d", fieldNo) : fieldNo + ""));
                baseConfigurationFeildFertigationModel.setLastEnabledFieldNo(fieldNo - 1);
                enableFieldFertigation.setVisibility(View.VISIBLE);
                disableFieldFertigation.setVisibility(View.INVISIBLE);
            }
            sendMessage(SmsServices.phoneNumber, smsdata);
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
                b = false;
                if (SmsServices.phoneNumber.replaceAll("\\s", "").equals(phoneNumber.replaceAll("\\s", "")) && !systemDown) {
                    checkSMS(message);
                    System.out.println("phoneNumber1 = " + phoneNumber);
                    System.out.println("phoneNumber2 = " + SmsServices.phoneNumber.trim());
                } else if (phoneNumber.contains(SmsServices.phoneNumber.replaceAll("\\s", "")) && !systemDown) {
                    // System.out.println("Screen 2.1\nSender's Number = " + phoneNumber + "\n Message : " + message);
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
                            startActivity(new Intent(Screen_6.this,MainActivity_GSM.class));
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
        startActivity(new Intent(Screen_6.this, Screen_4.class));
        finish();
    }

    public void checkSMS(String message) {
        enableEditText();
        if (message.toLowerCase().contains(SmsUtils.INSMS_6_1.toLowerCase())) {
            if (Integer.parseInt(message.substring(SmsUtils.INSMS_6_1.length()).trim()) == model.getFieldNo()) {
                status.setText("Fertigation Enabled");
                baseConfigurationFeildFertigationModel.setModelList(modelList);
                System.out.println(baseConfigurationFeildFertigationModel.getLastEnabledFieldNo());
                try {
                    curd_files.updateFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE, baseConfigurationFeildFertigationModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                initializeModel();
            }
        } else if (message.toLowerCase().contains(SmsUtils.INSMS_6_2.toLowerCase())) {
            status.setText("Wrong Fertigation time send, fertigation is not enabled");
        } else if (message.toLowerCase().contains(SmsUtils.INSMS_7_1.toLowerCase())) {
            status.setText("Fertigation Disabled");
        }


    }
}
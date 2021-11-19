package com.leledevelopers.smartirrigation;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.leledevelopers.smartirrigation.models.BaseConfigureFieldIrrigationModel;
import com.leledevelopers.smartirrigation.models.ConfigureFieldIrrigationModel;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Screen_5 extends SmsServices {
    private static final String TAG = Screen_5.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    ArrayAdapter<CharSequence> fieldNoArray, triggerFrom;
    private Spinner spinner, wetPeriod;
    private int hour, min;
    EditText valveOnPeriod, valveOffPeriod, soilDryness, soilWetness, priority, cycles;
    TextView status;
    private Button enableFertigation, disableFertigation, back_5, motorOnTime;
    private Boolean b, systemDown = false;
    private ConfigureFieldIrrigationModel model;
    private CURD_Files<ConfigureFieldIrrigationModel> curd_files = new CURD_FilesImpl<ConfigureFieldIrrigationModel>();
    private List<ConfigureFieldIrrigationModel> modelList = new ArrayList<ConfigureFieldIrrigationModel>();
    private BaseConfigureFieldIrrigationModel baseConfigureFieldIrrigationModel = new BaseConfigureFieldIrrigationModel();
    private SmsUtils smsUtils = new SmsUtils();
    private String regex = "\\d+";
    private int fieldNo;
    private boolean isEditedValveOnPeriod = false;
    private boolean isEditedValveOffPeriod = false;
    private boolean isEditedSoilDryness = false;
    private boolean isEditedSoilWetness = false;
    private boolean isEditedMotorOnTime = false;
    private boolean isEditedPriority = false;
    private boolean isEditedCycles = false;
    private boolean isEditedWetPeriod = false;
    private boolean isInitial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen5);
        initViews();
        // spinner of field no
        fieldNoArray = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        fieldNoArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(fieldNoArray);
        triggerFrom = ArrayAdapter.createFromResource(getApplicationContext(), R.array.triggerfrom, android.R.layout.simple_spinner_dropdown_item);
        wetPeriod.setAdapter(triggerFrom);
        initializeModel();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (modelList.get(position).isEnabled()) {
                    model = modelList.get(position);
                    System.out.println(model.toString());
                    valveOnPeriod.setText(model.getValveOnPeriod() + "");
                    valveOffPeriod.setText(model.getValveOffPeriod() + "");
                    soilDryness.setText(model.getSoilDryness() + "");
                    soilWetness.setText(model.getSoilWetness() + "");
                    motorOnTime.setText(model.getMotorOnTime());
                    priority.setText(model.getPriority() + "");
                    cycles.setText(model.getCycle() + "");
                    System.out.println("spinnerIntValue(model.getTiggerFrom() --> "+spinnerIntValue(model.getTiggerFrom()));
                    wetPeriod.setSelection(spinnerIntValue(model.getTiggerFrom()));
                    getHoursAndMinutes(model.getMotorOnTime());
                    disableFertigation.setVisibility(View.VISIBLE);
                    enableFertigation.setVisibility(View.INVISIBLE);
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
        /*wetPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/

        wetPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if ((position+"").equals(spinnerIntValue(model.getTiggerFrom()) + "")) {
                    System.out.println("model.getTiggerFrom() --> "+model.getTiggerFrom());
                    System.out.println(position+"");
                    isEditedWetPeriod = false;
                    isAnyViewEdited();
                } else {
                    System.out.println("model.getTiggerFrom() --> "+model.getTiggerFrom());
                    System.out.println(position+"");
                    isEditedWetPeriod = true;
                    isAnyViewEdited();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        valveOnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valveOnPeriod.setCursorVisible(true);
                System.out.println("-->1 " + valveOnPeriod.getText().toString());
                // System.out.println("-->1 "+model.getValveOnPeriod());
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (valveOnPeriod.getText().toString().equals(model.getValveOnPeriod() + "")) {
                    isEditedValveOnPeriod = false;
                    System.out.println("-->1 " + false);
                    isAnyViewEdited();
                } else {
                    System.out.println();
                    isEditedValveOnPeriod = true;
                    System.out.println("-->1 " + true);
                    isAnyViewEdited();
                }
            }
        });

        valveOffPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valveOffPeriod.setCursorVisible(true);
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (valveOffPeriod.getText().toString().equals(model.getValveOffPeriod() + "")) {
                    isEditedValveOffPeriod = false;
                    isAnyViewEdited();
                } else {
                    isEditedValveOffPeriod = true;
                    isAnyViewEdited();
                }
            }
        });

        soilDryness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soilDryness.setCursorVisible(true);
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (soilDryness.getText().toString().equals(model.getSoilDryness() + "")) {
                    isEditedSoilDryness = false;
                    isAnyViewEdited();
                } else {
                    isEditedSoilDryness = true;
                    isAnyViewEdited();
                }
            }
        });

        soilWetness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soilWetness.setCursorVisible(true);
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (soilWetness.getText().toString().equals(model.getSoilWetness() + "")) {
                    isEditedSoilWetness = false;
                    isAnyViewEdited();
                } else {
                    isEditedSoilWetness = true;
                    isAnyViewEdited();
                }
            }
        });

        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                priority.setCursorVisible(true);
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (priority.getText().toString().equals(model.getPriority() + "")) {
                    isEditedPriority = false;
                    isAnyViewEdited();
                } else {
                    isEditedPriority = true;
                    isAnyViewEdited();
                }
            }
        });

        cycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cycles.setCursorVisible(true);
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (cycles.getText().toString().equals(model.getCycle() + "")) {
                    isEditedCycles = false;
                    isAnyViewEdited();
                } else {
                    isEditedCycles = true;
                    isAnyViewEdited();
                }
            }
        });


        back_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                startActivity(new Intent(Screen_5.this, Screen_4.class));
                finish();
            }
        });

        // motor time
        motorOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    motorOnTime.clearFocus();

                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (motorOnTime.getText().toString().equals(model.getMotorOnTime() + "")) {
                    isEditedMotorOnTime = false;
                    isAnyViewEdited();
                } else {
                    isEditedMotorOnTime = true;
                    isAnyViewEdited();
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(Screen_5.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour = hourOfDay;
                                min = minute;
                                Calendar cal = Calendar.getInstance();
                                cal.set(0, 0, 0, hour, min);
                                motorOnTime.setText(DateFormat.format("HH:mm", cal));
                            }
                        }, 24, 0, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour, min);
                timePickerDialog.show();
            }
        });

        //enable fertigation
        enableFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (validateInput() && !systemDown) {
                    cursorVisibility();
                    updateData_And_SendSMS("enable");
                    smsReceiver.waitFor_1_Minute();
                    b = true;
                }
            }
        });

        //disable fertigation
        disableFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (!systemDown) {
                    updateData_And_SendSMS("disable");
                    smsReceiver.waitFor_1_Minute();
                    b = false;
                    status.setText("Field No " + fieldNo + " was disabled");
                }
            }
        });
        valveOnPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                    {

                    }
            }
        });
        valveOffPeriod.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {

                }
            }
        });
        soilDryness.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {

                }
            }
        });


    }
    @Override
    public void initViews() {
        spinner = (Spinner) findViewById(R.id.fieldNoSpinner5);
        valveOnPeriod = findViewById(R.id.valveOnPeriod);
        valveOffPeriod = findViewById(R.id.valveOffPeriod);
        soilDryness = findViewById(R.id.soilDryness);
        soilWetness = findViewById(R.id.soilWetness);
        motorOnTime = findViewById(R.id.motorOnTime);
        priority = findViewById(R.id.priority);
        cycles = findViewById(R.id.cycles);
        wetPeriod = findViewById(R.id.wetPeriod);
        enableFertigation = findViewById(R.id.enableFieldFertigation5);
        disableFertigation = findViewById(R.id.disableFertigation5);
        back_5 = findViewById(R.id.back_5);
        status = findViewById(R.id.screen_5_status);
    }

    private void cursorVisibility() {
        valveOnPeriod.setCursorVisible(false);
        valveOffPeriod.setCursorVisible(false);
        soilDryness.setCursorVisible(false);
        soilWetness.setCursorVisible(false);
        priority.setCursorVisible(false);
        cycles.setCursorVisible(false);

    }


    private void setEmptyData() {
        disableFertigation.setVisibility(View.INVISIBLE);
        enableFertigation.setVisibility(View.VISIBLE);
        valveOnPeriod.setText("");
        valveOffPeriod.setText("");
        soilDryness.setText("");
        soilWetness.setText("");
        motorOnTime.setText("");
        priority.setText("");
        cycles.setText("");
        model = null;
    }

    @Override
    public String toString() {
        return "Screen_5{" +
                "isEditedValveOnPeriod=" + isEditedValveOnPeriod +
                ", isEditedValveOffPeriod=" + isEditedValveOffPeriod +
                ", isEditedSoilDryness=" + isEditedSoilDryness +
                ", isEditedSoilWetness=" + isEditedSoilWetness +
                ", isEditedMotorOnTime=" + isEditedMotorOnTime +
                ", isEditedPriority=" + isEditedPriority +
                ", isEditedCycles=" + isEditedCycles +
                ", isEditedWetPeriod=" + isEditedWetPeriod +
                ", isInitial=" + isInitial +
                '}';
    }

    private void isAnyViewEdited() {
        System.out.println(toString());
        if (isEditedValveOnPeriod || isEditedValveOffPeriod || isEditedCycles || isEditedWetPeriod || isEditedPriority
                || isEditedSoilDryness || isEditedSoilWetness) {
            enableFertigation.setVisibility(View.VISIBLE);
            disableFertigation.setVisibility(View.INVISIBLE);
        } else {
            disableFertigation.setVisibility(View.VISIBLE);
            enableFertigation.setVisibility(View.INVISIBLE);

        }
    }


    private boolean validateInput() {

        if (spinner.getSelectedItem().toString().trim().equals("Pick one")) {
            spinner.requestFocus();
            return false;
        }
        if (!(valveOnPeriod.getText().toString().matches(regex)
                 && valveOnPeriod.getText().toString().length() >= 2 &&
                validateRange(10, 999, Integer.parseInt(valveOnPeriod.getText().toString())))) {
            valveOnPeriod.requestFocus();
            valveOnPeriod.getText().clear();
            valveOnPeriod.setError("please enter a valid value");
            return false;
        }
        if (!(valveOffPeriod.getText().toString().matches(regex)
                 && valveOnPeriod.getText().toString().length() >= 1 &&
                validateRange(1, 99, Integer.parseInt(valveOffPeriod.getText().toString())))) {
            valveOnPeriod.requestFocus();
            valveOnPeriod.getText().clear();
            valveOnPeriod.setError("please enter a valid value");
            return false;
        }
        if (!(soilDryness.getText().toString().matches(regex)
                 && soilDryness.getText().toString().length() >= 3 &&
                validateRange(100, 999, Integer.parseInt(soilDryness.getText().toString())))) {
            soilDryness.requestFocus();
            soilDryness.getText().clear();
            soilDryness.setError("please enter a valid value");
            return false;
        }
        if (!(soilWetness.getText().toString().matches(regex)
                && soilWetness.getText().toString().length() == 5 &&
                validateRange(10000, 99999, Integer.parseInt(soilWetness.getText().toString())))) {
            soilWetness.requestFocus();
            soilWetness.setError("please enter a valid value");
            soilWetness.getText().clear();
            return false;
        }
        if (motorOnTime.getText().toString() == "")  /// motor time validation is to be checked
        {
            motorOnTime.requestFocus();
            motorOnTime.setError("please enter a valid value");
            return false;
        }
        if (!(priority.getText().toString().matches(regex) &&
                 priority.getText().toString().length() >= 1 &&
                validateRange(1, 9, Integer.parseInt(priority.getText().toString())))) {
            priority.requestFocus();
            priority.setError("please enter a valid value");
            priority.getText().clear();
            return false;
        }
        if (!(cycles.getText().toString().matches(regex) &&
                cycles.getText().toString().length() == 1 && validateRange(1,9,Integer.parseInt(cycles.getText().toString())))) {
            cycles.requestFocus();
            cycles.getText().clear();
            cycles.setError("please enter a valid value");
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
            if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_IRRIGATION_FILE)) {
                System.out.println("--> data file");
                baseConfigureFieldIrrigationModel = (BaseConfigureFieldIrrigationModel) curd_files.getFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE);
                modelList = baseConfigureFieldIrrigationModel.getModelList();
                System.out.println("--> field no " + baseConfigureFieldIrrigationModel.getLastEnabledFieldNo());
                if (baseConfigureFieldIrrigationModel.getLastEnabledFieldNo() != -1) {
                    // isInitial = false;
                    model = modelList.get(baseConfigureFieldIrrigationModel.getLastEnabledFieldNo());
                    Toast.makeText(Screen_5.this, model.toString(), Toast.LENGTH_LONG).show();
                    if (model.isEnabled()) {
                        spinner.setSelection(model.getFieldNo() - 1);
                        valveOnPeriod.setText(model.getValveOnPeriod() + "");
                        valveOffPeriod.setText(model.getValveOffPeriod() + "");
                        soilDryness.setText(model.getSoilDryness() + "");
                        soilWetness.setText(model.getSoilWetness() + "");
                        motorOnTime.setText(model.getMotorOnTime());
                        priority.setText(model.getPriority() + "");
                        cycles.setText(model.getCycle() + "");
                        wetPeriod.setSelection(spinnerIntValue(model.getTiggerFrom()));
                        getHoursAndMinutes(model.getMotorOnTime());
                        disableFertigation.setVisibility(View.VISIBLE);
                        enableFertigation.setVisibility(View.INVISIBLE);
                    } else {
                        isInitial = true;
                        disableFertigation.setVisibility(View.INVISIBLE);
                        enableFertigation.setVisibility(View.VISIBLE);
                    }
                } else {
                    isInitial = true;
                    disableFertigation.setVisibility(View.INVISIBLE);
                    enableFertigation.setVisibility(View.VISIBLE);
                }
            } else {
                System.out.println("--> no data ");
                Toast.makeText(Screen_5.this, "NO data", Toast.LENGTH_LONG).show();
                for (int i = 0; i < 12; i++) {
                    ConfigureFieldIrrigationModel modelData = new ConfigureFieldIrrigationModel();
                    modelData.setFieldNo(i);
                    modelData.setEnabled(false);
                    modelList.add(modelData);
                    baseConfigureFieldIrrigationModel.setModelList(modelList);
                }
                curd_files.createFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE, baseConfigureFieldIrrigationModel);
                isInitial = true;
                setEmptyData();
                /*disableFertigation.setVisibility(View.INVISIBLE);
                enableFertigation.setVisibility(View.VISIBLE);*/
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getHoursAndMinutes(String motorOnTime) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(dateFormatter.parse(motorOnTime));
            String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            String mintue = String.valueOf(c.get(Calendar.MINUTE));
            model.setMotorOnTimeHr(Integer.parseInt(hour));
            model.setMotorOnTimeMins(Integer.parseInt(mintue));
            System.out.println("Hour: " + hour);
            System.out.println("Minute: " + mintue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateData_And_SendSMS(String typeOfAction) {
        if (!spinner.getSelectedItem().toString().trim().equals("Pick one")) {
            String smsdata;
            fieldNo = Integer.parseInt(spinner.getSelectedItem().toString());
            if (typeOfAction.equals("enable")) {
                model = modelList.get(fieldNo - 1);
                model.setFieldNo(Integer.parseInt(spinner.getSelectedItem().toString()));
                model.setValveOnPeriod(Integer.parseInt(valveOnPeriod.getText().toString()));
                model.setValveOffPeriod(Integer.parseInt(valveOffPeriod.getText().toString()));
                model.setSoilDryness(Integer.parseInt(soilDryness.getText().toString()));
                model.setSoilWetness(Integer.parseInt(soilWetness.getText().toString()));
                model.setMotorOnTime(motorOnTime.getText().toString());
                model.setPriority(Integer.parseInt(priority.getText().toString()));
                model.setCycle(Integer.parseInt(cycles.getText().toString()));
                model.setTiggerFrom((wetPeriod.getSelectedItem().toString()));
                getHoursAndMinutes(model.getMotorOnTime());
                model.setEnabled(true);
                System.out.println("after set " + model.toString());
                smsdata = smsUtils.OutSMS_4((model.getFieldNo()<10? String.format("%02d", model.getFieldNo()):model.getFieldNo()+""), model.getValveOnPeriod(), model.getValveOffPeriod()
                        , model.getMotorOnTimeHr(), model.getMotorOnTimeMins(), model.getSoilDryness(),
                        model.getSoilWetness(), model.getPriority(), model.getCycle(), spinnerIntValue(model.getTiggerFrom()));
                baseConfigureFieldIrrigationModel.setLastEnabledFieldNo(fieldNo - 1);
                enableFertigation.setVisibility(View.INVISIBLE);
                disableFertigation.setVisibility(View.INVISIBLE);
                isInitial = false;
            } else {
                smsdata = smsUtils.OutSMS_5((fieldNo<10? String.format("%02d", fieldNo):fieldNo+""));
                baseConfigureFieldIrrigationModel.setLastEnabledFieldNo(fieldNo - 1);
                enableFertigation.setVisibility(View.VISIBLE);
                disableFertigation.setVisibility(View.INVISIBLE);
            }
            sendMessage(SmsServices.phoneNumber, smsdata);
            modelList.set(fieldNo - 1, model);
            isEditedValveOnPeriod = false;
            isEditedValveOffPeriod = false;
            isEditedSoilDryness = false;
            isEditedSoilWetness = false;
            isEditedMotorOnTime = false;
            isEditedPriority = false;
            isEditedCycles = false;
            isEditedWetPeriod = false;
        } else {
            Toast.makeText(Screen_5.this, "Please select the field no", Toast.LENGTH_LONG).show();
            enableFertigation.setVisibility(View.VISIBLE);
        }
    }

    private int spinnerIntValue(String value) {
        int val = 0;
        switch (value) {
            case "Today":
                val = 0;
                break;
            case "Day+1":
                val = 1;
                break;
            case "Day+2":
                val = 2;
                break;
            case "Day+3":
                val = 3;
                break;
            case "Day+4":
                val = 4;
                break;
            case "Day+5":
                val = 5;
                break;
            case "Day+6":
                val = 6;
                break;
            case "Day+7":
                val = 7;
                break;
            default:
                System.out.println("");
        }
        return val;
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
                } else if(phoneNumber.contains(SmsServices.phoneNumber.replaceAll("\\s",""))  && !systemDown) {
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
        startActivity(new Intent(Screen_5.this,MainActivity_GSM.class));
        finish();
    }

    public void checkSMS(String message) {
        if (message.contains(SmsUtils.INSMS_4_1)) {
            status.setText(SmsUtils.INSMS_4_1);
            baseConfigureFieldIrrigationModel.setModelList(modelList);
            System.out.println("--> field no " + baseConfigureFieldIrrigationModel.getLastEnabledFieldNo());
            try {
                curd_files.updateFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE, baseConfigureFieldIrrigationModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
           // initializeModel();
        } else if (message.contains(SmsUtils.INSMS_5_1)) {
            status.setText("Valve configuration kept on Hold");
        }
       initializeModel();
    }
}
package com.leledevelopers.smartirrigation;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Screen_5 extends SmsServices {
    private static final String TAG = Screen_5.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    ArrayAdapter<CharSequence> adapter;
    private Spinner spinner;
    private int hour, min;
    EditText valveOnPeriod, valveOffPeriod, soilDryness, soilWetness, priority, cycles, wetPeriod;
    TextView status;
    private Button enableFertigation, disableFertigation,back_5,motorOnTime;
    private Boolean b,systemDown = false;
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
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        initializeModel();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (modelList.get(position).isEnabled()) {
                    disableFertigation.setVisibility(View.VISIBLE);
                    enableFertigation.setVisibility(View.INVISIBLE);
                    model = modelList.get(position);
                    System.out.println(model.toString());
                    valveOnPeriod.setText(model.getValveOnPeriod() + "");
                    valveOffPeriod.setText(model.getValveOffPeriod() + "");
                    soilDryness.setText(model.getSoilDryness() + "");
                    soilWetness.setText(model.getSoilWetness() + "");
                    motorOnTime.setText(model.getMotorOnTime());
                    priority.setText(model.getPriority() + "");
                    cycles.setText(model.getCycle() + "");
                    wetPeriod.setText(model.getTiggerFrom());
                    getHoursAndMinutes(model.getMotorOnTime());
                } else {
                    isInitial = true;
                    setEmptyData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        valveOnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("-->1 "+valveOnPeriod.getText().toString());
               // System.out.println("-->1 "+model.getValveOnPeriod());
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (valveOnPeriod.getText().toString().equals(model.getValveOnPeriod() + "")) {
                    isEditedValveOnPeriod = false;
                    System.out.println("-->1 "+false);
                } else {
                    isEditedValveOnPeriod = true;
                    System.out.println("-->1 "+true);

                }
                isAnyViewEdited();
            }
        });

        valveOffPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (valveOffPeriod.getText().toString().equals(model.getValveOffPeriod() + "")) {
                    isEditedValveOffPeriod = false;

                } else {
                    isEditedValveOffPeriod = true;
                }
                isAnyViewEdited();
            }
        });

        soilDryness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (soilDryness.getText().toString().equals(model.getSoilDryness() + "")) {
                    isEditedSoilDryness = false;

                } else {
                    isEditedSoilDryness = true;
                }
                isAnyViewEdited();
            }
        });

        soilWetness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (soilWetness.getText().toString().equals(model.getSoilWetness() + "")) {
                    isEditedSoilWetness = false;

                } else {
                    isEditedSoilWetness = true;
                }
                isAnyViewEdited();
            }
        });

        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (priority.getText().toString().equals(model.getPriority() + "")) {
                    isEditedPriority = false;

                } else {
                    isEditedPriority = true;
                }
                isAnyViewEdited();
            }
        });

        cycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (cycles.getText().toString().equals(model.getCycle() + "")) {
                    isEditedCycles = false;

                } else {
                    isEditedCycles = true;
                }
                isAnyViewEdited();
            }
        });

        wetPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (wetPeriod.getText().toString().equals(model.getTiggerFrom() + "")) {
                    isEditedWetPeriod = false;

                } else {
                    isEditedWetPeriod = true;
                }
                isAnyViewEdited();
            }
        });
        back_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_5.this,Screen_4.class));
                finish();
            }
        });

        // motor time
        motorOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("-->2 "+motorOnTime.getText().toString());
               // System.out.println("-->2 "+model.getMotorOnTime());
                /*if (isInitial) {
                    disableFertigation.setVisibility(View.INVISIBLE);
                } else if (motorOnTime.getText().toString().equals(model.getMotorOnTime() + "")) {
                    isEditedMotorOnTime = false;
                    System.out.println("-->2 false");
                } else {
                    isEditedMotorOnTime = true;
                    System.out.println("-->2 true");
                }
                isAnyViewEdited();*/
                TimePickerDialog timePickerDialog = new TimePickerDialog(Screen_5.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                hour = hourOfDay;
                                min = minute;
                                Calendar cal = Calendar.getInstance();
                                cal.set(0, 0, 0, hour, min);
                                motorOnTime.setText(DateFormat.format("hh:mm aa", cal));
                            }
                        }, 12, 0, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hour, min);
                timePickerDialog.show();
            }
        });

        //enable fertigation
        enableFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput() && !systemDown) {
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
                if(!systemDown) {
                    updateData_And_SendSMS("disable");
                    smsReceiver.waitFor_1_Minute();
                    b = true;
                }
            }
        });
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
        wetPeriod.setText("");
        model = null;
    }

    private void isAnyViewEdited() {
        if (isEditedValveOnPeriod || isEditedValveOffPeriod || isEditedCycles || isEditedWetPeriod || isEditedPriority
                || isEditedSoilDryness || isEditedSoilWetness) {
            disableFertigation.setVisibility(View.INVISIBLE);
            enableFertigation.setVisibility(View.VISIBLE);
        } else {
            disableFertigation.setVisibility(View.VISIBLE);
            enableFertigation.setVisibility(View.INVISIBLE);
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
                model.setTiggerFrom(wetPeriod.getText().toString());
                getHoursAndMinutes(model.getMotorOnTime());
                model.setEnabled(true);
                System.out.println("after set " + model.toString());
                smsdata = smsUtils.OutSMS_4(model.getFieldNo(), model.getValveOnPeriod(), model.getValveOffPeriod()
                        , model.getMotorOnTimeHr(), model.getMotorOnTimeMins(), model.getSoilDryness(),
                        model.getSoilWetness(), model.getPriority(), model.getCycle(), model.getTiggerFrom());
                baseConfigureFieldIrrigationModel.setLastEnabledFieldNo(fieldNo - 1);
                isInitial = false;
            } else {
                smsdata = smsUtils.OutSMS_5(fieldNo);
                baseConfigureFieldIrrigationModel.setLastEnabledFieldNo(fieldNo - 1);
            }
            sendMessage(SmsServices.phoneNumber, smsdata);
            modelList.set(fieldNo - 1, model);
            enableFertigation.setVisibility(View.INVISIBLE);
            disableFertigation.setVisibility(View.INVISIBLE);
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

    private boolean validateInput() {

        if (spinner.getSelectedItem().toString().trim().equals("Pick one")) {
            spinner.requestFocus();
            return false;
        }
        if (!(valveOnPeriod.getText().toString().matches(regex) && valveOnPeriod.getText().toString().length() >= 2)) {
            valveOnPeriod.requestFocus();
            valveOnPeriod.getText().clear();
            valveOnPeriod.setError("please enter a valid value");
            return false;
        }
        if (!(valveOffPeriod.getText().toString().matches(regex) && valveOnPeriod.getText().toString().length() >= 1)) {
            valveOnPeriod.requestFocus();
            valveOnPeriod.getText().clear();
            valveOnPeriod.setError("please enter a valid value");
            return false;
        }
        if (!(soilDryness.getText().toString().matches(regex)) && soilDryness.getText().toString().length() >= 3) {
            soilDryness.requestFocus();
            soilDryness.getText().clear();
            soilDryness.setError("please enter a valid value");
            return false;
        }
        if (!(soilWetness.getText().toString().matches(regex) && soilWetness.getText().toString().length() == 5)) {
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
        if (!(priority.getText().toString().matches(regex) && priority.getText().toString().length() >= 1)) {
            priority.requestFocus();
            priority.setError("please enter a valid value");
            priority.getText().clear();
            return false;
        }
        if (!(cycles.getText().toString().matches(regex) && cycles.getText().toString().length() == 1)) {
            cycles.requestFocus();
            cycles.getText().clear();
            cycles.setError("please enter a valid value");
            return false;
        }
        return true;
    }

    private void initializeModel() {

        try {
            if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_IRRIGATION_FILE)) {
                baseConfigureFieldIrrigationModel = (BaseConfigureFieldIrrigationModel) curd_files.getFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE);
                modelList = baseConfigureFieldIrrigationModel.getModelList();
                System.out.println("--> field no "+baseConfigureFieldIrrigationModel.getLastEnabledFieldNo());
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
                        wetPeriod.setText(model.getTiggerFrom());
                        getHoursAndMinutes(model.getMotorOnTime());
                        disableFertigation.setVisibility(View.VISIBLE);
                        enableFertigation.setVisibility(View.INVISIBLE);
                    } else {
                        isInitial = true;
                    }
                } else {
                    isInitial = true;
                }
            } else {
                Toast.makeText(Screen_5.this, "NO data", Toast.LENGTH_LONG).show();
                for (int i = 0; i < 12; i++) {
                    model = new ConfigureFieldIrrigationModel();
                    model.setFieldNo(i);
                    model.setEnabled(false);
                    modelList.add(new ConfigureFieldIrrigationModel());
                    baseConfigureFieldIrrigationModel.setModelList(modelList);
                    disableFertigation.setVisibility(View.INVISIBLE);
                    enableFertigation.setVisibility(View.VISIBLE);
                }
                curd_files.createFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE, baseConfigureFieldIrrigationModel);
                isInitial = true;
                disableFertigation.setVisibility(View.INVISIBLE);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getHoursAndMinutes(String motorOnTime) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm aa", Locale.ENGLISH);
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
        back_5=findViewById(R.id.back_5);
        status = findViewById(R.id.screen_5_status);
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

    public void checkSMS(String message) {
        if(message.contains(SmsUtils.INSMS_4_1)){
            status.setText(SmsUtils.INSMS_4_1);
            baseConfigureFieldIrrigationModel.setModelList(modelList);
            try {
                curd_files.updateFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE, baseConfigureFieldIrrigationModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(message.contains(SmsUtils.INSMS_5_1)){
            status.setText("Valve configuration kept on Hold");
        }
        initializeModel();
    }
}
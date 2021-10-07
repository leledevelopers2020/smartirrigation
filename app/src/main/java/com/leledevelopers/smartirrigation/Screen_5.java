package com.leledevelopers.smartirrigation;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Locale;

public class Screen_5 extends SmsServices {
    private static final String TAG = Screen_5.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    ArrayAdapter<CharSequence> adapter;
    private Spinner spinner;
    private int hour, min;
    EditText valveOnPeriod, valveOffPeriod, soilDryness, soilWetness, motorOnTime, priority, cycles, wetPeriod;
    TextView status;
    private Button enableFertigation, disableFertigation;
    private Boolean b;
    private ConfigureFieldIrrigationModel model;
    private CURD_Files curd_files = new CURD_FilesImpl();
    private SmsUtils smsUtils = new SmsUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen5);
        initViews();
        // spinner of field no
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        valveOnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFertigation.setVisibility(View.VISIBLE);
            }
        });

        valveOffPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFertigation.setVisibility(View.VISIBLE);
            }
        });

        soilDryness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFertigation.setVisibility(View.VISIBLE);
            }
        });

        soilWetness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFertigation.setVisibility(View.VISIBLE);
            }
        });

        priority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFertigation.setVisibility(View.VISIBLE);
            }
        });

        cycles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFertigation.setVisibility(View.VISIBLE);
            }
        });

        wetPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFertigation.setVisibility(View.VISIBLE);
            }
        });

        // motor time
        motorOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFertigation.setVisibility(View.VISIBLE);
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
                model.setFieldNo(Integer.parseInt(spinner.getSelectedItem().toString()));
                model.setValveOnPeriod(Integer.parseInt(valveOnPeriod.getText().toString()));
                model.setValveOffPeriod(Integer.parseInt(valveOffPeriod.getText().toString()));
                model.setSoilDryness(Integer.parseInt(soilDryness.getText().toString()));
                model.setSoilWetness(Integer.parseInt(soilWetness.getText().toString()));
                model.setMotorOnTime(motorOnTime.getText().toString());
                model.setPriority(Integer.parseInt(priority.getText().toString()));
                model.setCycle(Integer.parseInt(cycles.getText().toString()));
                model.setTiggerFrom(wetPeriod.getText().toString());
                System.out.println("after set " + model.toString());
                getHoursAndMinutes(model.getMotorOnTime());
                try {
                    curd_files.updateFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE, model);
                    String smsdata = smsUtils.OutSMS_4(model.getFieldNo(), model.getValveOnPeriod(), model.getValveOffPeriod()
                            , model.getMotorOnTimeHr(), model.getMotorOnTimeMins(), model.getSoilDryness(),
                            model.getSoilWetness(), model.getPriority(), model.getCycle(), model.getTiggerFrom());
                    sendMessage(SmsServices.phoneNumber, smsdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //disable fertigation
        disableFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFertigation.setVisibility(View.INVISIBLE);
                String smsdata = smsUtils.OutSMS_5(model.getFieldNo());
                sendMessage(SmsServices.phoneNumber, smsdata);
            }
        });

        initializeModel();
    }

    private void initializeModel() {
        if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_IRRIGATION_FILE)) {
            try {
                model = (ConfigureFieldIrrigationModel) curd_files.getFile(Screen_5.this, ProjectUtils.CONFG_IRRIGATION_FILE);
                Toast.makeText(Screen_5.this, model.toString(), Toast.LENGTH_LONG).show();
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
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(Screen_5.this, "NO data", Toast.LENGTH_LONG).show();
            model = new ConfigureFieldIrrigationModel();
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
                checkSMS(message);
            }

            @Override
            public void checkTime(String time) {
                if (b) {
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
        switch (message) {
            case SmsUtils.INSMS_4_1: {
                status.setText(SmsUtils.INSMS_4_1);
                break;
            }
            case SmsUtils.INSMS_5_1: {
                status.setText("Valve configuration kept on Hold");
                break;
            }
        }
    }
}
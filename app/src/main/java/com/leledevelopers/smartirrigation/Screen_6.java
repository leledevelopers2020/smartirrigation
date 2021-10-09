package com.leledevelopers.smartirrigation;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.leledevelopers.smartirrigation.models.ConfigurationFeildFertigationModel;
import com.leledevelopers.smartirrigation.services.CURD_Files;
import com.leledevelopers.smartirrigation.services.SmsReceiver;
import com.leledevelopers.smartirrigation.services.SmsServices;
import com.leledevelopers.smartirrigation.services.impl.CURD_FilesImpl;
import com.leledevelopers.smartirrigation.utils.ProjectUtils;
import com.leledevelopers.smartirrigation.utils.SmsUtils;

import java.io.IOException;

public class Screen_6 extends SmsServices {
    private static final String TAG = Screen_6.class.getSimpleName();
    private SmsReceiver smsReceiver = new SmsReceiver();
    private Boolean b;
    private Spinner spinner;
    EditText wetPeriod, injectPeriod, noOfIterations;
    private Button enableFieldFertigation, disableFieldFertigation;
    private ArrayAdapter<CharSequence> adapter;
    private TextView status;
    private ConfigurationFeildFertigationModel model;
    private CURD_Files curd_files = new CURD_FilesImpl();
    private SmsUtils smsUtils = new SmsUtils();
    private  String regex = "\\d+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen6);
        initViews();
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        wetPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFieldFertigation.setVisibility(View.VISIBLE);
            }
        });

        injectPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFieldFertigation.setVisibility(View.VISIBLE);
            }
        });

        noOfIterations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFieldFertigation.setVisibility(View.VISIBLE);
            }
        });

        enableFieldFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput())
                {
                    model.setFieldNo(Integer.parseInt(spinner.getSelectedItem().toString()));
                model.setWetPeriod(Integer.parseInt(wetPeriod.getText().toString()));
                model.setInjectPeriod(Integer.parseInt(injectPeriod.getText().toString()));
                model.setNoIterations(Integer.parseInt(noOfIterations.getText().toString()));
                System.out.println("after set " + model.toString());
                try {
                    curd_files.updateFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE, model);
                    String smsdata = smsUtils.OutSMS_6(model.getFieldNo(), model.getWetPeriod(),
                            model.getInjectPeriod(), model.getNoIterations());
                    sendMessage(SmsServices.phoneNumber, smsdata);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }
        });

        disableFieldFertigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFieldFertigation.setVisibility(View.INVISIBLE);
                String smsdata = smsUtils.OutSMS_7(model.getFieldNo());
                sendMessage(SmsServices.phoneNumber, smsdata);
            }
        });

        initializeModel();
    }

    private boolean validateInput() {
        if(spinner.getSelectedItem().toString().trim().equals("Pick one"))
        {
            spinner.requestFocus();
        }
        if(!(wetPeriod.getText().toString().matches(regex) && wetPeriod.getText().toString().length()>=1))
        {
            wetPeriod.requestFocus();
            wetPeriod.getText().clear();
            wetPeriod.setError("please enter a valid value");
            return false;
        }
        if(!(injectPeriod.getText().toString().matches(regex) && injectPeriod.getText().toString().length()>=1))
        {
            injectPeriod.requestFocus();
            injectPeriod.getText().clear();
            injectPeriod.setError("please enter a valid value");
            return false;
        }
        if(!(noOfIterations.getText().toString().matches(regex)) && noOfIterations.getText().toString().length()==1)
        {
            noOfIterations.requestFocus();
            noOfIterations.getText().clear();
            noOfIterations.setError("please enter a valid value");
            return false;
        }
        return true;
    }

    private void initializeModel() {
        if (curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_FERTIGATION_FILE)) {
            try {
                model = (ConfigurationFeildFertigationModel) curd_files.getFile(Screen_6.this, ProjectUtils.CONFG_FERTIGATION_FILE);
                Toast.makeText(Screen_6.this, model.toString(), Toast.LENGTH_LONG).show();
                spinner.setSelection(model.getFieldNo() - 1);
                wetPeriod.setText(model.getWetPeriod()+"");
                injectPeriod.setText(model.getInjectPeriod()+"");
                noOfIterations.setText(model.getNoIterations()+"");

            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(Screen_6.this, "NO data", Toast.LENGTH_LONG).show();
            model = new ConfigurationFeildFertigationModel();
        }
    }

    @Override
    public void initViews() {
        spinner = (Spinner) findViewById(R.id.fieldNoSpinner6);
        wetPeriod = findViewById(R.id.wetPeriod);
        injectPeriod = findViewById(R.id.injectPeriod);
        noOfIterations = findViewById(R.id.noOfIterations);
        enableFieldFertigation = findViewById(R.id.enableFieldFertigation6);
        disableFieldFertigation = findViewById(R.id.disableFieldFertigation6);
        status = findViewById(R.id.screen_6_status);
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
                System.out.println("phoneNumber1 = "+phoneNumber);
                System.out.println("phoneNumber2 = "+SmsServices.phoneNumber.trim());
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
            case SmsUtils.INSMS_6_1: {
                status.setText("Fertigation Enabled");
                break;
            }
            case SmsUtils.INSMS_6_2: {
                status.setText("Wrong Fertigation time send, fertigation is not enabled");
                break;
            }
            case SmsUtils.INSMS_7_1: {
                status.setText("Fertigation Disabled");
                break;
            }
        }
    }
}
package com.leledevelopers.smartirrigation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private Boolean b;
    EditText filtrationControlUnitNoDelay_1, filtrationControlUnitNoDelay_2, filtrationControlUnitNoDelay_3;
    EditText filtrationControlUnitOnTime, filtrationControlUnitSeparation;
    private Button enableFiltration, disableFiltration;
    private TextView status;
    private FiltrationModel model;
    private CURD_Files curd_files = new CURD_FilesImpl();
    private SmsUtils smsUtils = new SmsUtils();
    private  String regex = "\\d+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen7);
        initViews();
        filtrationControlUnitNoDelay_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFiltration.setVisibility(View.VISIBLE);
            }
        });
        filtrationControlUnitNoDelay_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFiltration.setVisibility(View.VISIBLE);
            }
        });
        filtrationControlUnitNoDelay_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFiltration.setVisibility(View.VISIBLE);
            }
        });
        filtrationControlUnitOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFiltration.setVisibility(View.VISIBLE);
            }
        });
        filtrationControlUnitSeparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFiltration.setVisibility(View.VISIBLE);
            }
        });
        enableFiltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput())
                {
                disableFiltration.setVisibility(View.VISIBLE);
                model.setFcDelay_1(Integer.parseInt(filtrationControlUnitNoDelay_1.getText().toString()));
                model.setFcDelay_2(Integer.parseInt(filtrationControlUnitNoDelay_2.getText().toString()));
                model.setFcDelay_3(Integer.parseInt(filtrationControlUnitNoDelay_3.getText().toString()));
                model.setFcOnTime(Integer.parseInt(filtrationControlUnitOnTime.getText().toString()));
                model.setFcSeperation(Integer.parseInt(filtrationControlUnitSeparation.getText().toString()));
                try {
                    curd_files.updateFile(getApplicationContext(), ProjectUtils.CONFG_FILTRATION_FILE, model);
                    String smsData = smsUtils.OutSMS_8(model.getFcDelay_1() + "", model.getFcDelay_2() + ""
                            , model.getFcDelay_3() + "", model.getFcOnTime() + "",
                            model.getFcSeperation() + "");
                    sendMessage(SmsServices.phoneNumber, smsData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }
        });
        disableFiltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableFiltration.setVisibility(View.VISIBLE);
                String smsData=smsUtils.OutSMS_9;
                sendMessage(SmsServices.phoneNumber,smsData);

            }
        });
        initializeModel();
    }

    private boolean validateInput() {
        if(!(filtrationControlUnitNoDelay_1.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_1.getText().toString().length()>=1))
        {
            filtrationControlUnitNoDelay_1.requestFocus();
            filtrationControlUnitNoDelay_1.getText().clear();
            filtrationControlUnitNoDelay_1.setError("please enter a valid value");
            return false;
        }
        if(!(filtrationControlUnitNoDelay_2.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_2.getText().toString().length()>=1))
        {
            filtrationControlUnitNoDelay_2.requestFocus();
            filtrationControlUnitNoDelay_2.getText().clear();
            filtrationControlUnitNoDelay_2.setError("please enter a valid value");
            return false;
        }
        if(!(filtrationControlUnitNoDelay_3.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_3.getText().toString().length()>=1))
        {
            filtrationControlUnitNoDelay_3.requestFocus();
            filtrationControlUnitNoDelay_3.getText().clear();
            filtrationControlUnitNoDelay_3.setError("please enter a valid value");
            return false;
        }
        if(!(filtrationControlUnitOnTime.getText().toString().matches(regex) &&
                filtrationControlUnitOnTime.getText().toString().length()>=1))
        {
            filtrationControlUnitOnTime.requestFocus();
            filtrationControlUnitOnTime.getText().clear();
            filtrationControlUnitOnTime.setError("please enter a valid value");
            return false;
        }
        if(!(filtrationControlUnitSeparation.getText().toString().matches(regex) &&
                filtrationControlUnitSeparation.getText().toString().length()>=2))
        {
            filtrationControlUnitSeparation.requestFocus();
            filtrationControlUnitSeparation.getText().clear();
            filtrationControlUnitSeparation.setError("please enter a valid value");
        }
        return true;
    }

    private void initializeModel() {
        if(curd_files.isFileHasData(getApplicationContext(), ProjectUtils.CONFG_FILTRATION_FILE))
        {
            try {
                model=(FiltrationModel) curd_files.getFile(getApplicationContext(),ProjectUtils.CONFG_FILTRATION_FILE);
                filtrationControlUnitNoDelay_1.setText(model.getFcDelay_1());
                filtrationControlUnitNoDelay_2.setText(model.getFcDelay_2());
                filtrationControlUnitNoDelay_3.setText(model.getFcDelay_3());
                filtrationControlUnitOnTime.setText(model.getFcOnTime());
                filtrationControlUnitSeparation.setText(model.getFcSeperation());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(Screen_7.this, "NO data", Toast.LENGTH_LONG).show();

        }
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
        status = findViewById(R.id.screen_7_status);
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
            case SmsUtils.INSMS_8_1: {
                status.setText("Pump Filtration Activated");
                break;
            }
            case SmsUtils.INSMS_9_1: {
                status.setText("Pump Filtration De-Activated");
                break;
            }
        }
    }
}
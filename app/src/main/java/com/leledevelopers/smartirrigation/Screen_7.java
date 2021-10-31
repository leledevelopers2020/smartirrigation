package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
                System.out.println("--->++---> " + filtrationControlUnitNoDelay_1.getText().toString() + " & " + model.getFcDelay_1());
                if (isInitial) {
                    disableFiltration.setVisibility(View.INVISIBLE);
                } else if (filtrationControlUnitNoDelay_1.getText().toString().equals(model.getFcDelay_1() + "")) {
                    System.out.println("--->++---> " + "yes");
                    isEditedDelay_1 = false;

                } else {
                    System.out.println("--->++---> " + "No");
                    isEditedDelay_1 = true;
                }
                isAnyViewEdited();
            }
        });
        filtrationControlUnitNoDelay_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("--->++---> " + filtrationControlUnitNoDelay_2.getText().toString() + " & " + model.getFcDelay_2());
                if (isInitial) {
                    disableFiltration.setVisibility(View.INVISIBLE);
                } else if (filtrationControlUnitNoDelay_2.getText().toString().equals(model.getFcDelay_2() + "")) {
                    System.out.println("--->++---> " + "yes");
                    isEditedDelay_2 = false;
                } else {
                    System.out.println("--->++---> " + "No");
                    isEditedDelay_2 = true;
                }
                isAnyViewEdited();
            }
        });
        filtrationControlUnitNoDelay_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("--->++---> " + filtrationControlUnitNoDelay_3.getText().toString() + " & " + model.getFcDelay_3());
                if (isInitial) {
                    disableFiltration.setVisibility(View.INVISIBLE);
                } else if (filtrationControlUnitNoDelay_3.getText().toString().equals(model.getFcDelay_3() + "")) {
                    System.out.println("--->++---> " + "yes");
                    isEditedDelay_3 = false;
                } else {
                    System.out.println("--->++---> " + "No");
                    isEditedDelay_3 = true;
                }
                isAnyViewEdited();
            }
        });
        filtrationControlUnitOnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("--->++---> " + filtrationControlUnitOnTime.getText().toString() + " & " + model.getFcOnTime());
                if (isInitial) {
                    disableFiltration.setVisibility(View.INVISIBLE);
                } else if (filtrationControlUnitOnTime.getText().toString().equals(model.getFcOnTime() + "")) {
                    System.out.println("--->++---> " + "yes");
                    isEditedOnTime = false;
                } else {
                    System.out.println("--->++---> " + "No");
                    isEditedOnTime = true;
                }
                isAnyViewEdited();
            }
        });
        filtrationControlUnitSeparation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("--->++---> " + filtrationControlUnitSeparation.getText().toString() + " & " + model.getFcSeperation());
                if (isInitial) {
                    disableFiltration.setVisibility(View.INVISIBLE);
                } else if (filtrationControlUnitSeparation.getText().toString().equals(model.getFcSeperation() + "")) {
                    System.out.println("--->++---> " + "yes");
                    isEditedSeparation = false;
                } else {
                    System.out.println("--->++---> " + "No");
                    isEditedSeparation = true;
                }
                isAnyViewEdited();
            }
        });
        enableFiltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput() && !systemDown) {
                    updateData_And_SendSMS("enable");
                    smsReceiver.waitFor_1_Minute();
                    b = true;
                }
            }
        });
        disableFiltration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!systemDown) {
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
                startActivity(new Intent(Screen_7.this, Screen_4.class));
                finish();
            }
        });

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
            disableFiltration.setVisibility(View.VISIBLE);
            enableFiltration.setVisibility(View.INVISIBLE);
        } else {
            disableFiltration.setVisibility(View.INVISIBLE);
            enableFiltration.setVisibility(View.VISIBLE);
        }
        // return (isEditedDelay_1 || isEditedDelay_2 || isEditedDelay_3 || isEditedOnTime || isEditedSeparation) ? true : false;
    }

    private boolean validateInput() {
        if (!(filtrationControlUnitNoDelay_1.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_1.getText().toString().length() >= 1)) {
            filtrationControlUnitNoDelay_1.requestFocus();
            filtrationControlUnitNoDelay_1.getText().clear();
            filtrationControlUnitNoDelay_1.setError("please enter a valid value");
            return false;
        }
        if (!(filtrationControlUnitNoDelay_2.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_2.getText().toString().length() >= 1)) {
            filtrationControlUnitNoDelay_2.requestFocus();
            filtrationControlUnitNoDelay_2.getText().clear();
            filtrationControlUnitNoDelay_2.setError("please enter a valid value");
            return false;
        }
        if (!(filtrationControlUnitNoDelay_3.getText().toString().matches(regex) &&
                filtrationControlUnitNoDelay_3.getText().toString().length() >= 1)) {
            filtrationControlUnitNoDelay_3.requestFocus();
            filtrationControlUnitNoDelay_3.getText().clear();
            filtrationControlUnitNoDelay_3.setError("please enter a valid value");
            return false;
        }
        if (!(filtrationControlUnitOnTime.getText().toString().matches(regex) &&
                filtrationControlUnitOnTime.getText().toString().length() >= 1)) {
            filtrationControlUnitOnTime.requestFocus();
            filtrationControlUnitOnTime.getText().clear();
            filtrationControlUnitOnTime.setError("please enter a valid value");
            return false;
        }
        if (!(filtrationControlUnitSeparation.getText().toString().matches(regex) &&
                filtrationControlUnitSeparation.getText().toString().length() >= 2)) {
            filtrationControlUnitSeparation.requestFocus();
            filtrationControlUnitSeparation.getText().clear();
            filtrationControlUnitSeparation.setError("please enter a valid value");
            return false;
        }
        return true;
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
                    disableFiltration.setVisibility(View.VISIBLE);
                    enableFiltration.setVisibility(View.INVISIBLE);
                } else {
                    System.out.println("isEnabled " + model.isEnabled());
                    filtrationControlUnitNoDelay_1.setText("");
                    filtrationControlUnitNoDelay_2.setText("");
                    filtrationControlUnitNoDelay_3.setText("");
                    filtrationControlUnitOnTime.setText("");
                    filtrationControlUnitSeparation.setText("");
                    disableFiltration.setVisibility(View.INVISIBLE);
                    enableFiltration.setVisibility(View.VISIBLE);
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
package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leledevelopers.smartirrigation.models.Message;
import com.leledevelopers.smartirrigation.services.MessageAdapters;
import com.leledevelopers.smartirrigation.services.SmsServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Screen_8 extends SmsServices {
    Button printFieldSMS, printAllSMM, back_8;
    ArrayAdapter<CharSequence> adapter1, adapter2;
    Spinner fieldSpinner, allSMSSpinner;
    RecyclerView recyclerView;
    List<Message> messages = new ArrayList<Message>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen8);
        initViews();
        this.context = getApplicationContext();
        try {
            readAllMessages();
            messages = getSMS();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        adapter1 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner.setAdapter(adapter1);


        adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.messgesFrom, android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        allSMSSpinner.setAdapter(adapter2);

        printSms();

        printFieldSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterMessages("fields");
            }
        });

        printAllSMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterMessages("date");
            }
        });

        back_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                startActivity(new Intent(Screen_8.this, Screen_4.class));
                finish();
            }
        });
    }

    private void filterMessages(String filterType) {
        List<Message> messageArrayList = new ArrayList<Message>();
        if (filterType.equals("fields")) {
            int fieldNo = Integer.parseInt(fieldSpinner.getSelectedItem().toString());
            String fieldValue = fieldNo < 10 ? String.format("%02d", fieldNo) : fieldNo + "";
            // System.out.println("fieldValue -->> "+fieldValue);
            for (int i = 0; i < messages.size(); i++) {
                System.out.println("--> " + messages.get(i).getAction().toLowerCase());
                if (messages.get(i).getAction().toLowerCase().contains("field no." + fieldValue)
                        || messages.get(i).getAction().toLowerCase().contains("field no. " + fieldValue)
                        || messages.get(i).getAction().contains("Wet Field Detected.")
                        || messages.get(i).getAction().contains("Phase failure detected, Suspending all Actions")) {
                    //System.out.println("---> "+messages.toString());
                    messageArrayList.add(messages.get(i));

                }
                if (i == messages.size() - 1) {
                    startRecyclerView(messageArrayList);
                }
            }
        } else if (filterType.equals("date")) {
            int selectedDate = spinnerIntValue(allSMSSpinner.getSelectedItem().toString());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar currentCal = Calendar.getInstance();
            String currentdate = dateFormat.format(currentCal.getTime());
            currentCal.add(Calendar.DATE, -selectedDate);
            String pastDate = dateFormat.format(currentCal.getTime());
            System.out.println("Selected Date = " + pastDate);

            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).getDate().equals(pastDate)) {
                    messageArrayList.add(messages.get(i));
                }
                if (i == messages.size() - 1) {
                    startRecyclerView(messageArrayList);
                }
            }
        }
    }

    private int spinnerIntValue(String value) {
        int val = 0;
        switch (value) {
            case "Today":
                val = 0;
                break;
            case "Yesterday":
                val = 1;
                break;
            case "Day-2":
                val = 2;
                break;
            case "Day-3":
                val = 3;
                break;
            case "Day-4":
                val = 4;
                break;
            case "Day-5":
                val = 5;
                break;
            case "Day-6":
                val = 6;
                break;
        }
        return val;
    }

    private void startRecyclerView(List<Message> messageList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MessageAdapters(messageList));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void printSms() {
        for (Message message : messages) {
            System.out.println(message.toString());
        }
    }

    @Override
    public void initViews() {
        fieldSpinner = findViewById(R.id.fieldNoSpinner8);
        allSMSSpinner = findViewById(R.id.fieldDateSpinner8);
        printFieldSMS = findViewById(R.id.printFieldSMM);
        printAllSMM = findViewById(R.id.printAllSMM);
        recyclerView = findViewById(R.id.displaySMS);
        back_8 = findViewById(R.id.back_8);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Screen_8.this, Screen_4.class));
        finish();
    }
}

//http://realembed.blogspot.com/2013/11/retrieve-sms-message-on-particular-date.html
//https://findnerd.com/list/view/How-to-read-sms-from-a-particular-date-in-Android/8556/
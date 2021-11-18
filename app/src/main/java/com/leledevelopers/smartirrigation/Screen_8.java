package com.leledevelopers.smartirrigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;

import com.leledevelopers.smartirrigation.models.Message;
import com.leledevelopers.smartirrigation.services.SmsServices;

import java.io.IOException;
import java.util.List;

public class Screen_8 extends SmsServices {
    Button printFieldSMS, printAllSMM, back_8;
    ArrayAdapter<CharSequence> adapter;
    Spinner fieldSpinner, allSMSSpinner;
    RecyclerView recyclerView;
    int fieldNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen8);
        initViews();
        this.context = getApplicationContext();
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner.setAdapter(adapter);
        fieldNo = Integer.parseInt(fieldSpinner.getSelectedItem().toString());


        printSms();

        back_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen_8.this, Screen_4.class));
                finish();
            }
        });
    }

    private void printSms() {
        try {
            List<Message> messages = getSMS();
            for (Message message : messages) {
                System.out.println(message.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initViews() {
        fieldSpinner = findViewById(R.id.fieldNoSpinner8);
        allSMSSpinner = findViewById(R.id.fieldDateSpinner8);
        printFieldSMS = findViewById(R.id.printFieldSMM);
        printAllSMM = findViewById(R.id.printAllSMM);
        recyclerView = findViewById(R.id.displaySMS);
    }
}

//http://realembed.blogspot.com/2013/11/retrieve-sms-message-on-particular-date.html
//https://findnerd.com/list/view/How-to-read-sms-from-a-particular-date-in-Android/8556/
package com.leledevelopers.smartirrigation;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.leledevelopers.smartirrigation.services.CalendarService;
import com.leledevelopers.smartirrigation.services.SmsServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Screen_8 extends SmsServices {
    Button fromDate, toDate, printData;
    ArrayAdapter<CharSequence> adapter;
    Spinner spinner;
    String startingDate, endingDate;
    int fieldNo;

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen8);
        initViews();
        adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.selctFieldNoArray, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        fieldNo = Integer.parseInt(spinner.getSelectedItem().toString());
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarService calendarService = new CalendarService(Screen_8.this);
                calendarService.dateCalculator();
                calendarService.setBaseCalendarService(new CalendarService.BaseCalendarService() {
                    @Override
                    public void onClickOk(String date) {
                        setStartingDate(date);
                        System.out.println("fromDate = " + getStartingDate());
                    }
                });
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarService calendarService = new CalendarService(Screen_8.this);
                calendarService.dateCalculator();
                calendarService.setBaseCalendarService(new CalendarService.BaseCalendarService() {
                    @Override
                    public void onClickOk(String date) {
                        setEndingDate(date);
                        System.out.println("fromDate = " +getEndingDate());
                    }
                });
            }
        });

        printData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ownerNumber = SmsServices.phoneNumber.replaceAll("\\s", "");

                System.out.println(getStartingDate() + " - " + getEndingDate());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date dateStart = null;
                Date dateEnd = null;
                try {
                    dateStart = formatter.parse(getStartingDate()+ "T00:00:00");
                    dateEnd = formatter.parse(getEndingDate()+ "T23:59:59");
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                // Now create the filter and query the messages.
                String filter = "date>=" + dateStart.getTime() + " and date<=" + dateEnd.getTime();
                System.out.println(filter);
                // final Uri SMS_INBOX = Uri.parse("content://sms");
                final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
                Cursor cursor = getContentResolver().query(SMS_INBOX, null, filter, null, null);
                List<String> items = new ArrayList<String>();

                while(cursor.moveToNext()) {

                    // Convert date to a readable format.
                    Calendar calendar = Calendar.getInstance();
                    String date =  cursor.getString(cursor.getColumnIndex("date"));
                    Long timestamp = Long.parseLong(date);
                    calendar.setTimeInMillis(timestamp);
                    Date finaldate = calendar.getTime();
                    String smsDate = finaldate.toString();
                    String smsBody = cursor.getString(cursor.getColumnIndex("body"));

                    String phoneNumber = cursor.getString(cursor.getColumnIndex("address"));
                    if(ownerNumber.equals(phoneNumber) && smsBody.contains("field no"+fieldNo)){
                        //if(phoneNumber.equals("+919014425331")){
                        System.out.println("---> "+/*"919014425331"*/ownerNumber+ " , "+phoneNumber);
                        items.add("From : " + ownerNumber +  phoneNumber + "\n" +
                                "Date Sent: " +    smsDate + "\n" +
                                "Message : " + smsBody + "\n");
                    }

                }
                System.out.println("SMs data "+items);
                System.out.println("SMs count "+items.size());

                cursor.close();
            }
        });
     /*   spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }
    @Override
    public void initViews() {
        fromDate = (Button) findViewById(R.id.fromDate);
        toDate = (Button) findViewById(R.id.toDate);
        spinner = (Spinner) findViewById(R.id.fieldNoSpinner8);
        printData = findViewById(R.id.printData);
    }
}

//http://realembed.blogspot.com/2013/11/retrieve-sms-message-on-particular-date.html
//https://findnerd.com/list/view/How-to-read-sms-from-a-particular-date-in-Android/8556/
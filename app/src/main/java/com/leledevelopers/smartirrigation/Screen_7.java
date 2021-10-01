package com.leledevelopers.smartirrigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Screen_7 extends AppCompatActivity {

    EditText filtrationControlUnitNoDelay_1,filtrationControlUnitNoDelay_2,filtrationControlUnitNoDelay_3;
    EditText filtrationControlUnitOnTime,filtrationControlUnitSeparation;
    private Button enableFiltration7,disableFiltration7;
    private TextView status7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen7);
    }
    private void initViews()
    {
        filtrationControlUnitNoDelay_1=findViewById(R.id.filtrationControlUnitNoDelay_1);
        filtrationControlUnitNoDelay_2=findViewById(R.id.filtrationControlUnitNoDelay_2);
        filtrationControlUnitNoDelay_3=findViewById(R.id.filtrationControlUnitNoDelay_3);
        filtrationControlUnitOnTime=findViewById(R.id.filtrationControlUnitOnTime);
        filtrationControlUnitSeparation=findViewById(R.id.filtrationControlUnitSeparation);
        enableFiltration7=findViewById(R.id.enableFiltration7);
        disableFiltration7=findViewById(R.id.disableFiltration7);
        status7=findViewById(R.id.screen_7_status);
    }
}
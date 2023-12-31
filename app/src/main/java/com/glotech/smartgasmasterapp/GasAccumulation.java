package com.glotech.smartgasmasterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import glotech.smartgasmasterapp.R;

public class GasAccumulation extends AppCompatActivity {

    private Button next, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_accumulation);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        next = findViewById(R.id.confirm_NewScan_button);
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GasAccumulation.this, ScanNewQRCode.class);
                startActivity(intent);
            }
        });
    }
}
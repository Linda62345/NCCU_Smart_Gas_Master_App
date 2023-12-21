package com.glotech.smartgasmasterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import glotech.smartgasmasterapp.R;

public class TakeOrder extends AppCompatActivity {

    private Button addOrder,backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        addOrder = findViewById(R.id.confirmOrderButton);
        backButton = findViewById(R.id.backButton);

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TakeOrder.this, OrderList.class);
//                startActivity(intent);
//            }
//        });

        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakeOrder.this, OrderList.class);
                startActivity(intent);
            }
        });
    }
}
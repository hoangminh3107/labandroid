package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lab1.asm.AsmActivity;
import com.example.lab1.lab6.RegisterActivity;
import com.example.lab1.lab7.ClientActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnLab3, btnAsm, btnLab7, btnLab6;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLab3 = findViewById(R.id.btn_lab3);
        btnAsm = findViewById(R.id.btn_asm);
        btnLab7 = findViewById(R.id.lab7);
        btnLab6 = findViewById(R.id.lab6);

        btnAsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AsmActivity.class));
            }
        });

        btnLab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Lab3Activity.class));
            }
        });

        btnLab7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ClientActivity.class));
            }
        });

        btnLab6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

}


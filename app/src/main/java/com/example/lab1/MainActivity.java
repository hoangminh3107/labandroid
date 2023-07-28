package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.lab1.asm.AsmActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnLab3, btnAsm;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLab3 = findViewById(R.id.btn_lab3);
        btnAsm = findViewById(R.id.btn_asm);

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
    }

}


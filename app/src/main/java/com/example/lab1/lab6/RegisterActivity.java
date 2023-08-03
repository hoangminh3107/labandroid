package com.example.lab1.lab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText edtMail, edtPass;
    Button btnRegister;
    FirebaseAuth fAuth;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtMail = findViewById(R.id.email);
        edtPass = findViewById(R.id.pass);
        btnRegister = findViewById(R.id.btndangki);
        tv = findViewById(R.id.txtRegister);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        fAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.createUserWithEmailAndPassword(edtMail.getText().toString(), edtPass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(RegisterActivity.this, "dang ki thanh cong", Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

}
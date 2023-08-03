package com.example.lab1.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab1.MainActivity;
import com.example.lab1.R;
import com.example.lab1.asm.AsmActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText edtMail, edtPass;
    Button btnlogin;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtMail = findViewById(R.id.email);
        edtPass = findViewById(R.id.pass);
        btnlogin = findViewById(R.id.btnlogin);
        firebaseAuth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = edtMail.getText().toString();
                String pass = edtPass.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(mail , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this , AsmActivity.class));
                            finishAffinity();
                        }else {
                            Toast.makeText(LoginActivity.this, "Email or Password  incorect ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
}
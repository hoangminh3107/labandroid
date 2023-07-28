package com.example.lab1.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.lab1.R;

public class Lab2Activity extends AppCompatActivity {

    private TextView textView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        HashingTask hashingTask = new HashingTask(handler);
        hashingTask.execute("a918c858d2dd1a3c69163267468804bdcd67daf50de8899183efe63e8412438a");
    }

    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(@NonNull Message message) {
           switch (message.what) {
                case 1:
                    String result = (String) message.obj;
                   
                    textView.setText(result);
                    break;
                default:
                    break;
            }
            return false;
        }
    });
}

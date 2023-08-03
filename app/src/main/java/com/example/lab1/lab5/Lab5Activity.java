package com.example.lab1.lab5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab1.MainActivity;
import com.example.lab1.R;
import com.example.lab1.lab7.ClientActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Lab5Activity extends AppCompatActivity {
    Button btnConnect;
    TextView tvReciveData;
    String serverName = "192.168.1.7";
    int port = 1234;
    SocketThread socketThread;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab5);
        btnConnect = findViewById(R.id.btnConnect);
        tvReciveData = findViewById(R.id.tv_recivedata);

        new Thread(() -> {
            try {
                Socket socket = new Socket(serverName, port);
                socketThread = new SocketThread(socket);
                socketThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private class SocketThread extends Thread {
        private Socket socket;
        private BufferedReader reader;

        public SocketThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {

                    final String finalLine = line;
                    runOnUiThread(() -> tvReciveData.setText(finalLine));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
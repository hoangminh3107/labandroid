package com.example.lab1.lab7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lab1.MainActivity;
import com.example.lab1.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientActivity extends AppCompatActivity {
    TextView tcRecivedata;
    String serverName = "192.168.1.7";
    int port = 1234;
    private SocketThread socketThread;

    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;
    private NotificationCompat.Builder builder;
    private final String channelId = "i.apps.notifications";
    private final String description = "Test notification";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        tcRecivedata = findViewById(R.id.message);


        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

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

                    if(reader.readLine().equals(reader.readLine())){
                        Intent intent = new Intent(ClientActivity.this, MainActivity.class);

                        PendingIntent pendingIntent = PendingIntent.getActivity(ClientActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationChannel = new NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.GREEN);
                            notificationChannel.enableVibration(false);
                            notificationManager.createNotificationChannel(notificationChannel);

                            builder = new NotificationCompat.Builder(ClientActivity.this, channelId)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setContentTitle(reader.readLine())
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                                    .setContentIntent(pendingIntent);
                            notificationManager.notify(1234, builder.build());
                        } else {
                            builder = new NotificationCompat.Builder(ClientActivity.this)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                                    .setContentIntent(pendingIntent);
                        }
                        notificationManager.notify(1234, builder.build());

                    }
                    final String finalLine = line;
                    runOnUiThread(() -> tcRecivedata.setText(finalLine));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
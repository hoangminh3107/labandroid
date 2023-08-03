package com.example.myapplicationserver;

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
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


/// ĐÂY LÀ APP NHẬN THÔNG BÁO SỐ ẢNH ĐÃ LƯU ĐƯỌC VÀO SERVER CỦA BÀI ASSIGNMENT
public class AppClientGetNotificationActivity extends AppCompatActivity {

    String serverName = "192.168.1.7";
    int port = 1234;
    private SocketThread socketThread;

    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;
    private NotificationCompat.Builder builder;
    private final String channelId = "i.apps.notifications";
    private final String description = "Test notification";

    public AppClientGetNotificationActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_client_get_notification);
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

    @Override
    protected void onRestart() {
        super.onRestart();
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

    @Override
    protected void onResume() {
        super.onResume();
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
                        Intent intent = new Intent(AppClientGetNotificationActivity.this, MainActivityServer.class);

                        PendingIntent pendingIntent = PendingIntent.getActivity(AppClientGetNotificationActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notificationChannel = new NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.GREEN);
                            notificationChannel.enableVibration(false);
                            notificationManager.createNotificationChannel(notificationChannel);

                            builder = new NotificationCompat.Builder(AppClientGetNotificationActivity.this, channelId)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setContentTitle(reader.readLine())
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                                    .setContentIntent(pendingIntent);
                            notificationManager.notify(1234, builder.build());
                        } else {
                            builder = new NotificationCompat.Builder(AppClientGetNotificationActivity.this)
                                    .setSmallIcon(R.drawable.ic_launcher_background)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                                    .setContentIntent(pendingIntent);
                            notificationManager.notify(1234, builder.build());
                        }
                        notificationManager.notify(1234, builder.build());

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
package com.example.myapplicationserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


// ĐÂY LÀ APP SERVER CHO LAB 5 VÀ LAB 7
public class MainActivityServer extends AppCompatActivity {
    TextView tvname, tvport, tvstatus;
    String serverip = "192.168.1.6";
    EditText edtmess;
    int serverport = 1234;
    Button start , stop;
    ServerThread serverThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_server);
        tvname = findViewById(R.id.ip);
        tvport = findViewById(R.id.port);
        tvstatus = findViewById(R.id.message);
        start = findViewById(R.id.startserver);
        stop = findViewById(R.id.stop);
        edtmess = findViewById(R.id.server);
        serverThread = new ServerThread();
        tvname.setText(serverip);
        tvport.setText(String.valueOf(serverport));
        start.setOnClickListener(view -> serverThread.startServer());

        stop.setOnClickListener(view -> serverThread.stopServer());

    }

    class ServerThread extends Thread implements Runnable {
        boolean isRunning;
        ServerSocket serverSocket;
        int count = 0;


        public void startServer() {

            isRunning = true;
            start();
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(serverport);

                runOnUiThread(() -> tvstatus.setText("Waiting for client to connect"));

                while (true) {
                    Socket socket = serverSocket.accept();
                    count++;
                    runOnUiThread(() -> tvstatus.setText(" connect to " + socket.getInetAddress() + "  " + socket.getLocalPort()));

                    PrintWriter output_Server = new PrintWriter(socket.getOutputStream());

                    while (true) {
                        String message = edtmess.getText().toString();
                        if (message != null && !message.isEmpty()) {
                            output_Server.write("Message from server " + message + "\n");
                            output_Server.flush();
                            runOnUiThread(() -> tvstatus.setText("Message sent to client: " + message));
                        }
                        Thread.sleep(1000);
                    }
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public  void  stopServer(){
            isRunning = false;
            new Thread(() -> {
                if(serverSocket!=null){
                    try {
                        serverSocket.close();
                        runOnUiThread(() -> tvstatus.setText("Server close"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }
}

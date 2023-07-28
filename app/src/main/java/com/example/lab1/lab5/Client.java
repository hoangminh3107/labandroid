package com.example.lab1.lab5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        final String serverHost = "localhost";
        int port = 9999;
        Socket socketClient = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            socketClient = new Socket(serverHost, port);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String responeLine;
        while ((responeLine= bufferedReader.readLine()) != null){
            System.out.println(responeLine);
            if (responeLine.indexOf("OK") != -1){
                break;
            }
            bufferedReader.close();
            bufferedWriter.close();
        }
    }
}

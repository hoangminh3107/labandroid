package com.example.lab1.lab5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket listener = null;
        int clientNumber = 0;
        int port = 9999;

        try {
            listener = new ServerSocket(port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            while (true) {
                Socket socket = listener.accept();

                new ServerThread(clientNumber, socket).start();
            }
        } finally {
            listener.close();
        }
    }
}


class ServerThread extends Thread {
    private int port;
    private Socket socket;

    public ServerThread(int port, Socket socket) {
        this.port = port;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println(" this is print ");
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (true) {
                System.out.println("Send a message ");
                String line = System.console().readLine();

                if (line.equals("quit")) {
                    System.out.println("disconnect");
                    break;
                }
                bw.write("Server : " + line);

                bw.newLine();

                bw.flush();

            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

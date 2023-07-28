package com.example.lab1.lab2;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class HashingTask extends AsyncTask<String, Void, String> {

    private Handler handler;

    public HashingTask(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... strings) {
        String input = strings[0];
        String hashedInput = hash(input);
        return hashedInput;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Message message = handler.obtainMessage(1, result);
        message.sendToTarget();
    }

    private String hash(String input) {
        return input;
    }
}

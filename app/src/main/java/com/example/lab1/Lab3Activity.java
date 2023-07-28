package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Lab3Activity extends AppCompatActivity {
    private TextView tvGetDataApi;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3);
        tvGetDataApi = findViewById(R.id.tv_getdata_api);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result = sendGetHttpUrlConnection();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvGetDataApi.setText(result);
                    }
                });
            }
        }).start();
    }


    void getDataUrl() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL("http://103.118.28.46:3000/get-quote");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            int status = connection.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                InputStream respone = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(respone, StandardCharsets.UTF_8);

                JsonReader jsonReader = new JsonReader(inputStreamReader);
                jsonReader.beginObject();
                String result = getValue("quote", jsonReader);
                tvGetDataApi.setText(result);
            }

        } catch (Exception e) {
            Log.e("Exception",e.getMessage());
        }
    }
    private String sendGetHttpUrlConnection(){
        String result = null;
        try {
            URL url = new URL("http://103.118.28.46:3000/get-list-quote");
            // mở giao thức Http
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // phương thức sử dụng là GET
            conn.setRequestMethod("GET");
            // config request
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                // xử lý dữ liệu trả về
                InputStream responseBody = conn.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, StandardCharsets.UTF_8);

                // Xử lý Json trong java
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginArray();
                result = jsonReader.nextString();
                jsonReader.endArray();
            } else {
                // TODO: Xử lý kết quả trả về là lỗi
            }
        }catch (Exception e) {
            Log.e("Exception",e.getMessage());
        }

        return result;
    }

    void postDataUrl() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(" http://103.118.28.46:3000/add-quote");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "ccccccccc");


            byte[] postData = jsonObject.toString().getBytes(StandardCharsets.UTF_8);

            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(postData);
            }
            int status = connection.getResponseCode();

            if (status == HttpURLConnection.HTTP_OK) {
                InputStream respone = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(respone, StandardCharsets.UTF_8);

                JsonReader jsonReader = new JsonReader(inputStreamReader);
                jsonReader.beginObject();
                String result = getValue("name", jsonReader);
                tvGetDataApi.setText(result);
            }


        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    String getValue(String key, JsonReader jsonReader) throws IOException {
        String value = "";
        while (jsonReader.hasNext()) {
            String k = jsonReader.nextName();
            if (k.equals(key)) {
                value = jsonReader.nextString();
                break;
            }
        }
        return value;
    }


}
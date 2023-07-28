package com.example.lab1.asm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.lab1.R;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AsmActivity extends AppCompatActivity {
    Button btnGetData, btnPost;
    List<NasaModel> nasaModelList;
    RecyclerView recyclerView;
    List<String> urlListData;
    int counter;
    NasaAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asm);
        nasaModelList = new ArrayList<>();
        urlListData = new ArrayList<>();
        btnGetData = findViewById(R.id.btn_lab3);
        btnPost = findViewById(R.id.btn_asm);
        recyclerView = findViewById(R.id.recyclerView);

        btnGetData.setOnClickListener(view -> {
            getDataFromApi();
        });


        btnPost.setOnClickListener(view -> {
            Log.e("urlListData", urlListData.toString());
            Log.e("nasaModelListPost", nasaModelList.toString());
            postDataToServer();
        });
    }

    String apacheEncode(String decodedStr) {
        byte[] decodedByteArr = decodedStr.getBytes(Charset.forName("UTF-8"));
        return Base64.encodeToString(decodedByteArr, 1);
    }

    void getDataFromApi() {
        String apiKey = "IP5pVaHaWYJXW1YFdrA03mXo4IayAmPLytGphJqi";
        List<String> dateList = new ArrayList<>();
        counter = 0;
        for (int i = 1; i <= 20; i++) {
            String date = String.format("2023-01-%02d", i);
            dateList.add(date);
        }

        List<String> urlList = new ArrayList<>();
        for (String dateData : dateList) {
            Call<NasaModel> call = RetrofitClient.getInstance("https://api.nasa.gov/")
                    .getMyApi().getData(apiKey, dateData);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<NasaModel> call, Response<NasaModel> response) {
                    counter++;
                    urlList.add(response.body().getUrl());
                    nasaModelList.add(response.body());
                    if (counter == dateList.size()) {
                        String[] encodedArr = new String[urlList.size()];
                        for (int i = 0; i < urlList.size(); i++) {
                            encodedArr[i] = apacheEncode(urlList.get(i));
                        }
                        urlListData.addAll(Arrays.asList(encodedArr));
                        adapter = new NasaAdapter(getApplicationContext(), nasaModelList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        postDataToServer();
                    }
                }
                @Override
                public void onFailure(Call<NasaModel> call, Throwable t) {
                    Log.e("err", t.getMessage());
                }
            });
        }
    }

    private void postDataToServer() {
        Thread thread = new Thread(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.137.1:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api = retrofit.create(Api.class);
            api.addbase64(urlListData).enqueue(new Callback<ApiResponeStatus>() {
                @Override
                public void onResponse(Call<ApiResponeStatus> call, Response<ApiResponeStatus> response) {
                    Toast.makeText(AsmActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ApiResponeStatus> call, Throwable t) {

                }
            });
        });
        thread.start();
    }
    }

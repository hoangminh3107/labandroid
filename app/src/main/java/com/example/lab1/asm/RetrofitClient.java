package com.example.lab1.asm;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{
    private static RetrofitClient instance = null;
    private Api myApi;

    private RetrofitClient(String url) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }

    public static synchronized RetrofitClient getInstance(String url) {
        if (instance == null) {
            instance = new RetrofitClient(url);
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }
}

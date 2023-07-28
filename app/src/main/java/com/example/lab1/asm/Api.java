package com.example.lab1.asm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api{
    @GET("planetary/apod")
    Call<NasaModel> getData(@Query("api_key") String apiKey, @Query("date") String date);

    @POST("addnasadata")
    Call<List<NasaModel>> createCart(@Body List<NasaModel> cart);

    @POST("adddurlbase64")
    Call<ApiResponeStatus> addbase64(@Body List<String> str);

    @POST("nasa")
    Call<List<NasaModel>> getnasa();
}

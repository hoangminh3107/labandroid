package com.example.lab1.asm;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api{

    // LẤY TỪ API NASA
    @GET("planetary/apod")
    Call<NasaModel> getData(@Query("api_key") String apiKey, @Query("date") String date);


    // THÊM DỮ LIỆU API NASA VÀO SERVER CỦA MÌNH
    @POST("addnasadata")
    Call<List<NasaModel>> createNasaModel(@Body List<NasaModel> cart);


    // THÊM DỮ LIỆU ĐÃ CONVERT
    @POST("adddurlbase64")
    Call<ApiResponeStatus> addbase64(@Body List<String> str);


    // LẤY DỮ LIỆU KHI ĐÃ ĐƯỢC THÊM
    @GET("/n/nasa")
    Call<List<NasaModel>> getnasa();

    // XÓA TỪNG ITEM TRONG SERVER THEO ID (Ở ĐÂY EM DÙNG GET ĐỂ XÓA Ở BÊN SERVER)
    @GET("n/delete/{id}")
    Call<List<NasaModel>> deletenasa(@Path("id") String id);

    @FormUrlEncoded
    @POST("n/editnasa")
    Call<List<NasaModel>> editnasa(@Field("id") String id,
                                   @Field("copyright") String copyright,
                                   @Field("date") String date,
                                   @Field("explanation") String explanation,
                                   @Field("hdurl") String hdurl,
                                   @Field("media_type") String mediatype,
                                   @Field("service_version") String serviceversion,
                                   @Field("title") String title,
                                   @Field("url") String url

                                   );
}

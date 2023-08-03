package com.example.lab1.asm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.lab1.R;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
    List<NasaModel> nasaModelList2;
    RecyclerView recyclerView;
    List<String> urlListData;
    int counter;
    NasaAdapter adapter;
    OnDataChangeListener onDataChangeListener;
    int serverport = 1234;
    ServerThread serverThread;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asm);
        nasaModelList = new ArrayList<>();
        urlListData = new ArrayList<>();
        btnGetData = findViewById(R.id.btn_lab3);
        btnPost = findViewById(R.id.btn_asm);
        nasaModelList2=new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        serverThread = new ServerThread();

        btnGetData.setOnClickListener(view -> {
            getDataFromApi();
            // SAU KHI ADD DATA LÊN SERVER THÌ GỬI ĐI THÔNG BÁO
            serverThread.startServer();

        });
      onDataChangeListener = new OnDataChangeListener() {
          @Override
          public void onDataChanged(String id) {
              Retrofit retrofit = new Retrofit.Builder()
                      .baseUrl("https://alert-fawn-leggings.cyclic.cloud/")
                      .addConverterFactory(GsonConverterFactory.create())
                      .build();
              Api api = retrofit.create(Api.class);
              api.deletenasa(id).enqueue(new Callback<List<NasaModel>>() {
                  @Override
                  public void onResponse(Call<List<NasaModel>> call, Response<List<NasaModel>> response) {
                      getData();// load lai data
                  }

                  @Override
                  public void onFailure(Call<List<NasaModel>> call, Throwable t) {
                      getData();// load lai data
                  }
              });
          }

          @Override
          public void onDataUpdate(NasaModel nasaModel) {
              Intent intent = new Intent(AsmActivity.this, EditDataActivity.class);
              intent.putExtra("nasamodel",  nasaModel);
              startActivity(intent);
          }
      };

    }

    // CONVERT SANG BASE 64
    String apacheEncode(String decodedStr) {
        byte[] decodedByteArr = decodedStr.getBytes(Charset.forName("UTF-8"));
        return Base64.encodeToString(decodedByteArr, 1);
    }



    // GET DATA TỪ  API NASA
    void getDataFromApi() {
        String apiKey = "IP5pVaHaWYJXW1YFdrA03mXo4IayAmPLytGphJqi";
        List<String> dateList = new ArrayList<>();
        counter = 0;
        for (int i = 1; i <= 20; i++) {
            String date = String.format("2023-01-%02d", i);
            dateList.add(date);
        }

        List<String> urlList = new ArrayList<>();

        // POST DATA LÊN SERVER NODEJS
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
                        postDataToServer(nasaModelList);

                    }
                }

                @Override
                public void onFailure(Call<NasaModel> call, Throwable t) {
                    Log.e("err", t.getMessage());
                }
            });
        }
    }

    // HÀM POST DATA LÊN SERVER NODEJS
    private void postDataToServer(List<NasaModel> list) {

        // THREAD NÀY LÀ POST ẢNH ĐÃ CONVERT SANG BASE64 LÊN SERVER
        Thread thread = new Thread(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://alert-fawn-leggings.cyclic.cloud/")
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

        //THREAD NÀY LÀ LẤY DỮ LIỆU TỪ SERVER HIỂN THỊ KHI VÀO APP
        Thread thread2 = new Thread(() -> {
            getData();
        });

        // THREAD NÀY LÀ ĐỂ POST DỮ LIỆU CỦA API NASA LÊN SERVER GỒM CÁC TRƯỜNG NHƯ 'copyright', 'date', 'explanation'...
        Thread thread1 = new Thread(() -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://alert-fawn-leggings.cyclic.cloud/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api = retrofit.create(Api.class);
            api.createNasaModel(list).enqueue(new Callback<List<NasaModel>>() {
                @Override
                public void onResponse(Call<List<NasaModel>> call, Response<List<NasaModel>> response) {
                    thread2.start();
                    Log.e("respone", response.body().toString());
                }

                @Override
                public void onFailure(Call<List<NasaModel>> call, Throwable t) {

                }
            });
        });


        thread.start();
        thread1.start();

    }


    // HÀM LẤY DỮ LIỆU TỪ SERVER
    void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://alert-fawn-leggings.cyclic.cloud/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        api.getnasa().enqueue(new Callback<List<NasaModel>>() {
            @Override
            public void onResponse(Call<List<NasaModel>> call, Response<List<NasaModel>> response) {
                nasaModelList2 = response.body();
                adapter = new NasaAdapter(getApplicationContext(), nasaModelList2,onDataChangeListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<NasaModel>> call, Throwable t) {

            }
        });
    }


    // GỬI THÔNG BÁO ĐẾN APP KHÁC
    class ServerThread extends Thread implements Runnable {
        boolean isRunning;
        ServerSocket serverSocket;


        public void startServer() {

            isRunning = true;
            start();
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(serverport);
                while (true) {
                    Socket socket = serverSocket.accept();

                    PrintWriter output_Server = new PrintWriter(socket.getOutputStream());

                    while (true) {
                        String message = String.valueOf(urlListData.size());
                        if (message != null && !message.isEmpty()) {
                            output_Server.write("Số Lượng Ảnh Đã Lưu Về Server Là :" + message + "\n");
                            output_Server.flush();
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
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}

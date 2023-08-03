package com.example.lab1.asm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lab1.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditDataActivity extends AppCompatActivity {
    EditText editText1, editText2, editText3 , editText4,editText5,editText6,editText7,editText8;
    Button btnEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        editText1 = findViewById(R.id.edt1);
        editText2 = findViewById(R.id.edt2);
        editText3 = findViewById(R.id.edt3);
        editText4 = findViewById(R.id.edt4);
        editText5 = findViewById(R.id.edt5);
        editText6 = findViewById(R.id.edt6);
        editText7 = findViewById(R.id.edt7);
        editText8 = findViewById(R.id.edt8);
        btnEdit = findViewById(R.id.sua);

        NasaModel nasaModel = (NasaModel) getIntent().getSerializableExtra("nasamodel");
        editText1.setText(nasaModel.getCopyright());
        editText2.setText(nasaModel.getDate());
        editText3.setText(nasaModel.getExplanation());
        editText4.setText(nasaModel.getHdurl());
        editText4.setText(nasaModel.getMedia_type());
        editText4.setText(nasaModel.getService_version());
        editText4.setText(nasaModel.getTitle());
        editText4.setText(nasaModel.getUrl());




        btnEdit.setOnClickListener(view -> {
            String copy , date, ex,hdurl , media , version , title, url;
            copy = editText1.getText().toString();
            date = editText2.getText().toString();
            ex = editText3.getText().toString();
            hdurl = editText4.getText().toString();
            media = editText5.getText().toString();
            version = editText6.getText().toString();
            title = editText7.getText().toString();
            url = editText8.getText().toString();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://alert-fawn-leggings.cyclic.cloud/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api = retrofit.create(Api.class);
            api.editnasa(nasaModel.get_id(),copy,date,ex,hdurl,media,version,title,url
            ).enqueue(new Callback<List<NasaModel>>() {
                @Override
                public void onResponse(Call<List<NasaModel>> call, Response<List<NasaModel>> response) {
                    Toast.makeText(EditDataActivity.this, "Update thanh cong", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<NasaModel>> call, Throwable t) {

                }
            });
        });


    }
}
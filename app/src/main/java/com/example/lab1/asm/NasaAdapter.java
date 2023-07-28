package com.example.lab1.asm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab1.R;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NasaAdapter extends RecyclerView.Adapter<NasaAdapter.ViewHolder> {
    Context context;
    List<NasaModel> cartList;

    public NasaAdapter(Context context, List<NasaModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NasaModel oder = cartList.get(position);
        holder.tv1.setText("copyright :"+oder.getCopyright());
        holder.tv2.setText("date:"+oder.getDate());
        holder.tv3.setText("explanation :"+oder.getExplanation());
        holder.tv4.setText("hdurl : "+oder.getHdurl());
        holder.tv5.setText("media_type : "+oder.getMedia_type());
        holder.tv6.setText("service_version : "+oder.getService_version());
        holder.tv7.setText("title : "+oder.getTitle());
        holder.tv8.setText("url : "+oder.getUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("data", cartList.get(holder.getAdapterPosition()).getUrl().toString());
                Toast.makeText(context, "sád", Toast.LENGTH_SHORT).show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.137.1:3000/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Api api = retrofit.create(Api.class);
                api.createCart(cartList).enqueue(new Callback<List<NasaModel>>() {
                    @Override
                    public void onResponse(Call<List<NasaModel>> call, Response<List<NasaModel>> response) {
                        Log.e("aa", response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<List<NasaModel>> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2, tv3, tv4,tv5,tv6,tv7,tv8,tv9;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            tv4 = itemView.findViewById(R.id.tv4);
            tv5 = itemView.findViewById(R.id.tv5);
            tv6 = itemView.findViewById(R.id.tv6);
            tv7 = itemView.findViewById(R.id.tv7);
            tv8 = itemView.findViewById(R.id.tv8);
            tv9 = itemView.findViewById(R.id.tv9);

        }
    }
}

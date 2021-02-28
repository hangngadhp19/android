package com.example.nganth.restaurantapp.restaurant;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nganth.restaurantapp.Foods;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.databinding.ItemMenuBinding;
import com.example.nganth.restaurantapp.Restaurant;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter {
    private ArrayList<Foods> foods;

    public MenuAdapter(ArrayList<Foods> foods) {
    this.foods = foods;
}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup recyclerView, int viewType) {
        // Lấy layout trong thư mục res
        // đối tượng hỗ trợ truy xuất tài nguyên layout trong thư mục res/layout
        LayoutInflater layoutInflater = LayoutInflater.from(recyclerView.getContext());
        // binding đến layout mà mình muốn lấy
        ItemMenuBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_menu, recyclerView, false);
        // lấy layout ra
        View layout = binding.getRoot();

        // cung cấp cho ViewHolder
        MyViewHolder viewHolder = new MyViewHolder(layout);
        return viewHolder;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // vị trí vừa click
                    int position = getAdapterPosition();
                    Toast.makeText(view.getContext(), String.valueOf(position), Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemMenuBinding binding = DataBindingUtil.findBinding(holder.itemView);

        Foods data = foods.get(position);

        binding.lblResNameMenu.setText(data.getName());

        if (TextUtils.isEmpty(data.getImage())) {
            binding.imgFood.setImageResource(R.drawable.food_menu);
        } else {
            Context context = holder.itemView.getContext();
            Picasso.with(context).load(data.getImage()).placeholder(R.mipmap.ic_launcher).into(binding.imgFood);
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
}

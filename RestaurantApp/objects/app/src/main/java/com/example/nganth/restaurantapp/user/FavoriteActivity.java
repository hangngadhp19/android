package com.example.nganth.restaurantapp.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.nganth.restaurantapp.BaseActivity;
import com.example.nganth.restaurantapp.database.FavoritesTable;
import com.example.nganth.restaurantapp.database.MySqliteOpenHelper;
import com.example.nganth.restaurantapp.Restaurant;

import java.util.ArrayList;


public class FavoriteActivity extends BaseActivity {
    private SQLiteDatabase database;
    private com.example.nganth.restaurantapp.databinding.FavoriteBinding binding;

    ArrayList<Restaurant> restaurants = new ArrayList<>();
    private FavoriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = android.databinding.DataBindingUtil.setContentView(this, com.example.nganth.restaurantapp.R.layout.favorite);

        MySqliteOpenHelper openHelper = new
                MySqliteOpenHelper(getApplicationContext());

        database = openHelper.getWritableDatabase();

        FavoritesTable favoritesTable = new FavoritesTable(this);
        restaurants.addAll(favoritesTable.find());

        // Khoi tao Adapter
        adapter = new FavoriteAdapter(restaurants);

        adapter.onItemClick(new FavoriteAdapter.Callback() {
            @Override
            public void onItemSelected(int position, String value) {
                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
            }
        });

        // Cung cap Adapter cho RecyclerView
        binding.lstRestaurant.setAdapter(adapter);

        // Thiet lap dang hien thi cho RecyclerView - dang danh sach
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        binding.lstRestaurant.setLayoutManager(linearLayoutManager);
    }

    public void openProfileActivity(android.view.View view) {
        android.content.Intent intent = new android.content.Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }


}

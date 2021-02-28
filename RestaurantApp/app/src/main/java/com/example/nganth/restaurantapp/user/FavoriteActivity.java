package com.example.nganth.restaurantapp.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nganth.restaurantapp.BaseActivity;
import com.example.nganth.restaurantapp.MySqliteOpenHelper;
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
        find(null);

        // data is object
        restaurants.add(new Restaurant("Nha hang 1","Dia chi 1", null));
        restaurants.add(new Restaurant("Nha hang 2","Dia chi 2", null));
        restaurants.add(new Restaurant("Nha hang 3","Dia chi 3", null));
        restaurants.add(new Restaurant("Nha hang 4","Dia chi 4", null));
        restaurants.add(new Restaurant("Nha hang 5","Dia chi 5", null));
        restaurants.add(new Restaurant("Nha hang 6","Dia chi 6", null));
        restaurants.add(new Restaurant("Nha hang 7","Dia chi 7", null));

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

    public void find(View view) {
        Cursor cursor = database.query("favorites",null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndex("id"); // == 0;
                String resName = cursor.getString(1);
//                Toast.makeText(getApplicationContext(), resName, Toast.LENGTH_LONG).show();
//                Log.e("Restaurant Name: ", resName);


//                String text = binding.textView.getText().toString();
//                binding.textView.setText(text + "\r\n" + hoTen);
            }
        }
    }

    private void insert() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", 1);// thêm giá trị cho cột ma
        contentValues.put("res_name", "Nguyen Van An");// thêm giá trị cho cột hoten
        contentValues.put("res_add", "HCM");// thêm giá trị cho cột diachi
        long id = database.insert(
                "favorites", // tên bảng
                null,
                contentValues // dữ liệu của 1 dòng
        );

        Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
//        Log.e("TQKy", String.valueOf(id));
    }

    public void delete() {
        int count = database.delete("favorites",
                "id = 1",
                null
        );
        Toast.makeText(getApplicationContext(), String.valueOf(count), Toast.LENGTH_LONG).show();
//        Log.e("TQKy", String.valueOf(count));
    }
}

package com.example.nganth.restaurantapp.restaurant;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.nganth.restaurantapp.BaseActivity;
import com.example.nganth.restaurantapp.Place;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.databinding.RestaurantBinding;

import java.util.ArrayList;

public class RestaurantActivity extends BaseActivity {

    private RestaurantBinding binding;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    public ArrayList<Place> places = new ArrayList<>();
    public Double currentLat;
    public Double currentLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        binding = DataBindingUtil.setContentView(this, R.layout.restaurant);

        Intent intent = getIntent();
        if (intent != null) {
            String flag = intent.getStringExtra("flag");
            if (flag == null) {
                showWalkthought(null);
            } else {
                switch (flag) {
                    case "Walkthout":
                        showWalkthought(null);
                        break;
                    case "Search":
                        places = (ArrayList<Place>) intent.getSerializableExtra("EXTRA_PLACES");
                        currentLat = intent.getDoubleExtra("lat",0.0);
                        currentLng = intent.getDoubleExtra("lng",0.0);

                        showSearch(null);
                }
            }
        }
    }

    public void showWalkthought(View view) {
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        WalkthoughtFragment fragment = new WalkthoughtFragment();
//        transaction.replace(R.id.fragmentRestaurant, fragment);
//        transaction.commit();

        android.content.Intent intent = new android.content.Intent(getApplicationContext(), com.example.nganth.restaurantapp.restaurant.ViewPagerWalkthoughtActivity.class);
        startActivity(intent);
    }

    public void showSearch(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SearchFragment fragment = new SearchFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("EXTRA_PLACES",places);
        bundle.putDouble("lat", currentLat);
        bundle.putDouble("lng", currentLng);
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragmentRestaurant, fragment);
        transaction.commit();
    }

    public void showResult(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ResultFragment fragment = new ResultFragment();
        transaction.replace(R.id.fragmentRestaurant, fragment);
        transaction.commit();
    }

    public void showMenuByLink(View view) {
        android.content.Intent intent = new android.content.Intent(getApplicationContext(), com.example.nganth.restaurantapp.restaurant.ViewPagerMenuActivity.class);
        startActivity(intent);
    }
}

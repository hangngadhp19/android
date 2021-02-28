package com.example.nganth.restaurantapp.restaurant;

import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nganth.restaurantapp.BaseActivity;
import com.example.nganth.restaurantapp.Place;
import com.example.nganth.restaurantapp.PlacesService;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.databinding.ActivityViewPagerWalkthoughtBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewPagerWalkthoughtActivity extends BaseActivity {
    private String TAG = "Walkthought";
    private ActivityViewPagerWalkthoughtBinding binding;
    private float percentagePadding = 0.05f;

    private int page = -1;

    private PagerWalkthoughtAdapter adapter;
    private Handler handler;

    public ArrayList<Place> places = new ArrayList<>();
    public Double currentLat;
    public Double currentLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_pager_walkthought);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Serializable serializable = bundle.getSerializable("EXTRA_PLACES");
            if (serializable != null) {
                places = (ArrayList<Place>) serializable;
            }
            currentLat = bundle.getDouble("lat");
            currentLng = bundle.getDouble("lng");
        }

        binding.viewPagerWalkthought.setAdapter(adapter = new PagerWalkthoughtAdapter(getSupportFragmentManager(), places));
        binding.viewPagerWalkthought.setClipToPadding(false);

        binding.viewPagerWalkthought.post(new Runnable() {
            @Override
            public void run() {
                float padding = percentagePadding * binding.viewPagerWalkthought.getWidth();
                binding.viewPagerWalkthought.setPadding((int) (padding * 2), 0, (int) (padding * 2), 0);
                binding.viewPagerWalkthought.setPageMargin((int) padding);
            }
        });

        final int interval = 2000; // 2 Second
        handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                if (page == -1) {
                    binding.viewPagerWalkthought.setCurrentItem(++page, false);
                } else {
                    binding.viewPagerWalkthought.setCurrentItem(++page);
                }

                if (page == adapter.getCount() - 1) {
                    page = -1;
                }

                handler.postDelayed(this, interval);
            }
        };
        handler.postAtTime(runnable, System.currentTimeMillis() + interval);
        handler.postDelayed(runnable, interval);

    }

    public void showSearchMap(View view) {
        android.content.Intent intent = new android.content.Intent(getApplicationContext(), com.example.nganth.restaurantapp.restaurant.RestaurantActivity.class);
        intent.putExtra("lat", currentLat);
        intent.putExtra("lng", currentLng);
        intent.putExtra("EXTRA_PLACES", places);
        intent.putExtra("flag", "Search");

        startActivity(intent);
    }

}

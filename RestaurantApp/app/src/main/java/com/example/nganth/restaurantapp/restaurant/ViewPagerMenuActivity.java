package com.example.nganth.restaurantapp.restaurant;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.nganth.restaurantapp.BaseActivity;
//import com.example.nganth.restaurantapp.Manifest;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.databinding.ActivityViewPagerMenuBinding;

//https://developers.google.com/places/web-service/details
//-- get restaurant detail
//https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJ15256JXBiYgRJe9BObOLjtM&fields=photo,name,rating,formatted_phone_number,formatted_address,url,website,place_id&key=AIzaSyCEOvWIiRye57Hwi6nQoTkL7FuXX0--0xs
//-- get restaurant's photo
//https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CmRaAAAAX-LywgyVrefPrIbLJOb7okwPLPkNCIr6WojhpWlBf72oPxQ8zZp8FEa58CdlUsw4v2rP15yHclWr1cu7qwqUC7QIFwxQivvoX6i4xdGFe3XU0sJ6ZVWxUee0xchf2FcyEhDCrBfRpbidfULwLhIsZj01GhSs_onrfczoUCE2qNI1Z6xOR5SWGw&key=AIzaSyCEOvWIiRye57Hwi6nQoTkL7FuXX0--0xs


public class ViewPagerMenuActivity extends BaseActivity {
    private ActivityViewPagerMenuBinding binding;
    private FragmentManager fragmentManager;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_pager_menu);

        Intent intent = getIntent();
        if (intent != null) {
            page = intent.getIntExtra("pageNumber", 0);
        }

        PagerMenuAdapter adapter = new PagerMenuAdapter(getSupportFragmentManager());

        binding.viewPagerMenu.setAdapter(adapter);

        binding.viewPagerMenu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < binding.tabLayoutMenu.getTabCount(); i++) {
                    TabLayout.Tab tab = binding.tabLayoutMenu.getTabAt(i);

                    TextView view = (TextView) tab.getCustomView();

                    if (i == position) {
                        view.setTextColor(getResources().getColor(R.color.colorOrange));
                    } else {
                        view.setTextColor(Color.BLACK);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        binding.tabLayoutMenu.setupWithViewPager(binding.viewPagerMenu);

        //binding.tabLayoutMenu.set

        for (int i = 0; i < binding.tabLayoutMenu.getTabCount(); i++) {
            TabLayout.Tab tab = binding.tabLayoutMenu.getTabAt(i);

            TextView textView = new TextView(getApplicationContext());
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);

            if (i == 0) {
                textView.setText("Menu");
                textView.setTextColor(getResources().getColor(R.color.colorOrange));
            }
            else if (i == 1) {
                textView.setText("About");
            }
            else {
                textView.setText("Review");
            }
            tab.setCustomView(textView);
        }

        binding.viewPagerMenu.setCurrentItem(page);
    }

    public void showAbout(View view) {
        android.content.Intent intent = new android.content.Intent(getApplicationContext(), com.example.nganth.restaurantapp.restaurant.ViewPagerMenuActivity.class);
        startActivity(intent);
    }

    public void showMenu(View view) {
        android.content.Intent intent = new android.content.Intent(getApplicationContext(), com.example.nganth.restaurantapp.restaurant.ViewPagerMenuActivity.class);
        startActivity(intent);
    }

    public void showReview(View view) {
        android.content.Intent intent = new android.content.Intent(getApplicationContext(), com.example.nganth.restaurantapp.restaurant.ViewPagerMenuActivity.class);
        startActivity(intent);
    }

    public void openFavoriteActivity(View view) {
        android.content.Intent intent = new android.content.Intent(getApplicationContext(), com.example.nganth.restaurantapp.user.FavoriteActivity.class);
        startActivity(intent);
    }
}

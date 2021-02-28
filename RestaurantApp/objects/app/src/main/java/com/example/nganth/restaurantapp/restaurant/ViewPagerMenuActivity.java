package com.example.nganth.restaurantapp.restaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nganth.restaurantapp.BaseActivity;
//import com.example.nganth.restaurantapp.Manifest;
import com.example.nganth.restaurantapp.DataRestaurant;
import com.example.nganth.restaurantapp.MainActivity;
import com.example.nganth.restaurantapp.Place;
import com.example.nganth.restaurantapp.PlacesService;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.Restaurant;
import com.example.nganth.restaurantapp.database.FavoritesTable;
import com.example.nganth.restaurantapp.databinding.ActivityViewPagerMenuBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//https://developers.google.com/places/web-service/details
//-- get restaurant detail
//https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJ15256JXBiYgRJe9BObOLjtM&fields=photo,name,rating,formatted_phone_number,formatted_address,url,website,place_id&key=AIzaSyCEOvWIiRye57Hwi6nQoTkL7FuXX0--0xs
//-- get restaurant's photo
//https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CmRaAAAAX-LywgyVrefPrIbLJOb7okwPLPkNCIr6WojhpWlBf72oPxQ8zZp8FEa58CdlUsw4v2rP15yHclWr1cu7qwqUC7QIFwxQivvoX6i4xdGFe3XU0sJ6ZVWxUee0xchf2FcyEhDCrBfRpbidfULwLhIsZj01GhSs_onrfczoUCE2qNI1Z6xOR5SWGw&key=AIzaSyCEOvWIiRye57Hwi6nQoTkL7FuXX0--0xs


public class ViewPagerMenuActivity extends BaseActivity {
    private ActivityViewPagerMenuBinding binding;
    private FragmentManager fragmentManager;
    private int page = 0;
    public ArrayList<Place> restaurant_api = new ArrayList<>();
    public Place place;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    Restaurant restaurant;
    FavoritesTable favoritesTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_pager_menu);
        binding.setVariableMenu(this);

        Intent intent = getIntent();
        if (intent != null) {
            page = intent.getIntExtra("pageNumber", 0);
            place = (Place) intent.getSerializableExtra("place");
            if (place != null) {
                //--region: gan du lieu restaurant vao field
                binding.txtResNameInMenuPage.setText(place.getName());
                binding.txtResAddressInMenuPage.setText(place.getFormatted_address());
                //--endregion: gan du lieu restaurant vao field
            }
        }

        /*PlacesService.getResFromAPI(this, "ChIJ15256JXBiYgRJe9BObOLjtM", new PlacesService.RestaurantCallback() {
            @Override
            public void result(Place place) {
                if (place == null) return;
                ViewPagerMenuActivity.this.place = place;
                //--region: gan du lieu restaurant vao field
                binding.txtResNameInMenuPage.setText(place.getName());
                binding.txtResAddressInMenuPage.setText(place.getFormatted_address());
                //--endregion: gan du lieu restaurant vao field
            }
        });*/

        //-- get data from database sqlite
        if (place != null)
        {
            favoritesTable = new FavoritesTable(this);
            restaurant = favoritesTable.find_where(place.getPlaceId());
        }

        //-- test to show icon favorite
        if (restaurant != null) {
            ImageView img = (ImageView) findViewById(R.id.imageView4);
            img.setImageResource(R.drawable.ic_heart_orange);
            binding.button.setVisibility(View.GONE);
        }


        PagerMenuAdapter adapter = new PagerMenuAdapter(getSupportFragmentManager());

        binding.viewPagerMenu.setAdapter(adapter);

        binding.viewPagerMenu.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //region demo show notification
                if (position == 1) {
                    sendBroadcast(new Intent("notify"));
                } else {
                    sendBroadcast(new Intent("close"));
                }

                //endregion
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

    public void showWeb(View view) {
        android.content.Intent intent = new android.content.Intent(getApplicationContext(), com.example.nganth.restaurantapp.restaurant.WebViewActivity.class);
        TextView textView = (TextView) view;
        intent.putExtra("url", textView.getText().toString());
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

    public void addFavorites(View view) {
        if (place == null) return;

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        if (restaurant == null) {
//            Log.d("NTHN testing: ", String.valueOf(place.getRatting()));
            long result = favoritesTable.insert(new Restaurant(place.getPlaceId(), place.getName(), place.getFormatted_address(), place.getReference(), mAuth.getCurrentUser().getEmail(), place.getRatting()));

            if (result > 0) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.announcement)
                        .setMessage(R.string.announcement_sucess)
                        .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ImageView img = (ImageView) findViewById(R.id.imageView4);
                                img.setImageResource(R.drawable.ic_heart_orange);
                                binding.button.setVisibility(View.GONE);
                            }
                        }).show();
            }
        }
    }
}

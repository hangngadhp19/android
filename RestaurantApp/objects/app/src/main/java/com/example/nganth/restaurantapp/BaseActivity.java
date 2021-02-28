package com.example.nganth.restaurantapp;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.nganth.restaurantapp.restaurant.ViewPagerMenuActivity;
import com.example.nganth.restaurantapp.restaurant.ViewPagerWalkthoughtActivity;
import com.example.nganth.restaurantapp.user.FavoriteActivity;
import com.example.nganth.restaurantapp.user.ProfileActivity;
import com.example.nganth.restaurantapp.user.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    public FirebaseUser user;
    @VisibleForTesting
    public ProgressDialog mProgressDialog;
//    public String activityName = null;

    protected void attachBaseContext(Context newBase) {
        // đọc thông tin cấu hình đã lưu xuống file
        SharedPreferences sharedPreferences = newBase.getSharedPreferences("config", MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang", "en");


        Configuration configuration = newBase.getResources().getConfiguration();
        Locale locale = new Locale(lang);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);

            Context newContext = newBase.createConfigurationContext(configuration);
            super.attachBaseContext(newContext);
        } else {// thiet ngon ngu cho phien ban duoi 17
            configuration.locale = locale;
            newBase.getResources().updateConfiguration(configuration, newBase.getResources().getDisplayMetrics());
            super.attachBaseContext(newBase);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START initialize_auth]
        user = FirebaseAuth.getInstance().getCurrentUser();
        // [END initialize_auth]
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        /*if(user != null){
            inflater.inflate(R.menu.menu_main, menu);
        }else{
            inflater.inflate(R.layout.menu_login, menu);
        }*/

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Đối tượng tạo file lưu trữ config
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);

        // đối tượng cung cấp các phương thức để ghi dữ liệu xuống file
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }else if(id == R.id.action_profile){
            //Open profile user
            android.content.Intent intent = new android.content.Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        }else if(id == R.id.action_favorite){
            //Open favorite user
            android.content.Intent intent = new android.content.Intent(getApplicationContext(), FavoriteActivity.class);
            startActivity(intent);
        }else if(id == R.id.action_walkthought){
            //Open walkthought
//            android.content.Intent intent = new android.content.Intent(getApplicationContext(), RestaurantActivity.class);
            android.content.Intent intent = new android.content.Intent(getApplicationContext(), MainActivity.class);
//            intent.putExtra("flag", "Walkthout");
            startActivity(intent);
        }
//        else if(id == R.id.action_menu_res){
//            //Open restaurant menu
//            android.content.Intent intent = new android.content.Intent(getApplicationContext(), ViewPagerMenuActivity.class);
//            intent.putExtra("pageNumber", 0);
//            startActivity(intent);
//        }
        else if(id == R.id.action_lang_en){
            //Change language to english
            Toast.makeText(getApplicationContext(), "English", Toast.LENGTH_LONG).show();
            // lưu trữ dữ liệu theo tên truy xuất
            editor.putString("lang", "en");
            // thực thi ghi dữ liệu xuống file
            editor.apply();

            // đóng activity và chạy lại activity
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        else if(id == R.id.action_lang_ja){
            //Change language to japan
            Toast.makeText(getApplicationContext(), "Japan", Toast.LENGTH_LONG).show();
            // lưu trữ dữ liệu theo tên truy xuất
            editor.putString("lang", "ja");
            // thực thi ghi dữ liệu xuống file
            editor.apply();

            //-- use for language keep every screen
            // đóng activity và chạy lại activity
            finish();
            /*switch (activityName) {
                case "MainActivity":

                    break;
            }*/

            Intent intent = new Intent(this, MainActivity.class);
            //-- use for language keep every screen
//            intent.putExtra("fragmentName", 1);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        //Open signin user
        android.content.Intent intent = new android.content.Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}

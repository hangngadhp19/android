package com.example.nganth.restaurantapp;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class MyApplication extends Application {
    public final String CONFIG_NAME = "config";
    public final String CONFIG_LANG_KEY = "lang";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(setLocale(base));
    }

    public Context setLocale(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, MODE_PRIVATE);
        String lang = sharedPreferences.getString(CONFIG_LANG_KEY, "ja");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, lang);
        }
        return updateResourcesLegacy(context, lang);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}

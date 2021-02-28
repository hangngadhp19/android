package com.example.nganth.restaurantapp;

import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nganth.restaurantapp.restaurant.PagerWalkthoughtAdapter;
import com.example.nganth.restaurantapp.restaurant.ViewPagerWalkthoughtActivity;
import com.example.nganth.restaurantapp.user.ProfileActivity;
import com.example.nganth.restaurantapp.user.SignInActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private String TAG = "Mainactivity";
    private com.example.nganth.restaurantapp.databinding.HomeBinding binding;

    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 100000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 50000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Double currentLat;
    private Double currentLng;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    public ArrayList<Place> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = android.databinding.DataBindingUtil.setContentView(this, R.layout.home);
        init();


        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if( user != null && user.isEmailVerified()){
                    android.content.Intent intent = new android.content.Intent(getApplicationContext(), ViewPagerWalkthoughtActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("EXTRA_PLACES",places);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    android.content.Intent intent = new android.content.Intent(getApplicationContext(), com.example.nganth.restaurantapp.user.SignInActivity.class);
                    startActivity(intent);
                }
            }
        }, 1500);*/
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mSettingsClient = LocationServices.getSettingsClient(getApplicationContext());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                updateLocationUI();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            }
        };

        mRequestingLocationUpdates = true;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        String location  = "Lat: " + mCurrentLocation.getLatitude() + "Lng: " + mCurrentLocation.getLongitude();
        Toast.makeText(getApplication(), location, Toast.LENGTH_LONG).show();
        if(currentLat == null || currentLng == null ||  currentLat != mCurrentLocation.getLatitude() || currentLng != mCurrentLocation.getLongitude()) {
            currentLat = mCurrentLocation.getLatitude();
            currentLng = mCurrentLocation.getLongitude();

            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {

                        //Get restaurant
                       // 16.062708, 108.179480
                        places = PlacesService.search("restaurant", mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1000);
                        Log.d("debug", "Lat,long"+ mCurrentLocation.getLatitude() +"***"+ mCurrentLocation.getLongitude()+"place size"+ places.size());
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if( user != null && user.isEmailVerified() && places != null){
                            android.content.Intent intent = new android.content.Intent(getApplicationContext(), ViewPagerWalkthoughtActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("EXTRA_PLACES",places);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{
                            android.content.Intent intent = new android.content.Intent(getApplicationContext(), com.example.nganth.restaurantapp.user.SignInActivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
            thread.start();
        }
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getParent(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resuming location updates depending on button state and
        // allowed permissions
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        }else{

            return true;
            // Write you code here if permission already given.
        }
        /*int permissionState;
        permissionState = ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;*/
    }
}

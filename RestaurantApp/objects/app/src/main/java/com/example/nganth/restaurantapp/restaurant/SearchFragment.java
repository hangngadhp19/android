package com.example.nganth.restaurantapp.restaurant;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nganth.restaurantapp.PlacesService;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.Restaurant;
import com.example.nganth.restaurantapp.databinding.SearchBinding;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements OnMapReadyCallback {
    private String TAG = "Search Fragment";

    private GoogleMap mMap;

    //widgets
    private AutoCompleteTextView mSearchText;
    //Auto complete
    protected GeoDataClient mGeoDataClient;
    private ImageView mGps;
    private ImageView mIconSearch;
    private Button mClearText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136)
    );

    private static final float DEFAULT_ZOOM = 15.0f;


    public ArrayList<com.example.nganth.restaurantapp.Place> restaurants = new ArrayList<>();

    public Double currentLat;
    public Double currentLng;
    ArrayList<Marker> restaurantMakers = new ArrayList<>();

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            addRestaurantMarker();
            return true;
        }
    });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.search);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search, container, false);

        mSearchText = (AutoCompleteTextView) view.findViewById(R.id.input_search);
        mGps = (FloatingActionButton) view.findViewById(R.id.ic_gps);
        mIconSearch = (ImageView) view.findViewById(R.id.ic_magnify);
        /* Event clear text button */
        mClearText = (Button) view.findViewById(R.id.btn_clear);
        mClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText.setText("");
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        //MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }
    private void initSearch() {
        // Register a listener that receives callbacks when a suggestion has been selected
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        // Construct a GeoDataClient for the Google Places API for Android.
        mGeoDataClient = Places.getGeoDataClient(super.getActivity());

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(super.getActivity(), mGeoDataClient, LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);

        mIconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    //execute our method for searching
                    geoLocate();
                }
                return false;
            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       //         getDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle bundle = getArguments();
        restaurants = (ArrayList<com.example.nganth.restaurantapp.Place>) bundle.getSerializable("EXTRA_PLACES");
        currentLat = bundle.getDouble("lat");
        currentLng = bundle.getDouble("lng");

        moveCamera(new LatLng(currentLat, currentLng), DEFAULT_ZOOM, "Your location");
        //Log.d("restaurant i:", "(Length: "+restaurants.size()+")"+restaurants.get(0).getLat()+"***"+restaurants.get(0).getLng());
        addRestaurantMarker();

        initSearch();
    }

    private void addRestaurantMarker(){
        /*if(restaurantMakers != null){
            for (int i = 0; i < restaurantMakers.size(); i++) {
                restaurantMakers.get(i).remove();
                restaurantMakers.remove(i);
            }
        }*/
        restaurantMakers.clear();
        //restaurantMakers = new ArrayList<>();
        //add marker restaurants
        if(restaurants.size() >0){
            for (int i = 0; i < restaurants.size(); i++) {
                Marker maker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(restaurants.get(i).getLat(),restaurants.get(i).getLng()))
                        .title(restaurants.get(i).getName())
                        .snippet(restaurants.get(i).getFormatted_address())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant)));

                restaurantMakers.add(maker);
            }
        }
    }
    private void geoLocate() {
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(super.getActivity());
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOexception: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: found address" + address.toString());

           // mCurrentAddress.setText(address.getLocality().toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getLocality());
        }
    }
    private void moveCamera(LatLng latlng, float zoom, String title) {

        mMap.addMarker(new MarkerOptions().position(latlng).title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, DEFAULT_ZOOM));

        /*mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(final Marker marker) {
                TextView textView = new TextView(getContext());
                textView.setText(marker.getTitle());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_LONG).show();



                    }
                });
                return textView;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });*/

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getContext(), ViewPagerMenuActivity.class);
                Restaurant restaurant = (Restaurant) marker.getTag();
                if (restaurant != null) {
                    intent.putExtra("place", restaurant);
                }
                Log.d("restaurant nga_testing:", "(Length: "+restaurants.size()+")"+restaurants.get(0).getLat()+"***"+restaurants.get(0).getLng());
                getActivity().startActivity(intent);
            }
        });

        // Zoom in, animating the camera.
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());

        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latlng)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
       // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        hideSoftKeyboard();
    }
    private void hideSoftKeyboard(){
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager)super.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
    }

    /*
     * ***********************************************************************************
     * AUTO COMPLETE SEARCH LOCATION
     * ***********************************************************************************
     * */

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data Client
     * to retrieve more details about the place.
     *
     * @see GeoDataClient#getPlaceById(String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

            Toast.makeText(SearchFragment.super.getActivity(), "Clicked: " + primaryText,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data Client query that shows the first place result in
     * the details view on screen.
     */
    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                hideSoftKeyboard();

                PlaceBufferResponse places = task.getResult();
                if(places.getCount() > 0){
                    // Get the Place object from the buffer.
                    Place place = places.get(0);

                    //geoLocate();
                    final LatLng latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                    moveCamera(latLng, DEFAULT_ZOOM, "New position");
                    restaurants.clear();

                    Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                //Get restaurant 16.06673,108.211981
                                restaurants = PlacesService.search("restaurant", latLng.latitude, latLng.longitude, 1000);
                                Log.d(TAG, "Run place service search.");
                                //addRestaurantMarker();
                                handler.sendEmptyMessage(1);

                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                    thread.start();
                }
                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        return Html.fromHtml(res.getString(R.string.place_details, address));

    }
}

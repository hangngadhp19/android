package com.example.nganth.restaurantapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nganth.restaurantapp.Place;
import com.google.gson.Gson;

/**
 * @author saxman
 */
public class PlacesService {
    private static final String LOG_TAG = "PlacesService";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/search";

    private static final String OUT_JSON = "/json";

    // KEY!
    private static final String API_KEY = "AIzaSyCEOvWIiRye57Hwi6nQoTkL7FuXX0--0xs";


    //region demo callback
    public interface RestaurantCallback {
        void result(Place place);
    }
    public static void getResFromAPI(Context context, String placeId, final RestaurantCallback callback) {
        if (callback == null) return;;

        //region: get restaurant from api
        // link:
        // https://maps.googleapis.com/maps/api/place/details/json?
        // placeid=ChIJ15256JXBiYgRJe9BObOLjtM&
        // fields=photo,name,rating,formatted_phone_number,formatted_address,url,website,place_id,geometry&
        // key=AIzaSyCEOvWIiRye57Hwi6nQoTkL7FuXX0--0xs

        String params = "photo,name,rating,formatted_phone_number,formatted_address,url,website,place_id,geometry";
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://maps.googleapis.com/maps/api/place/details/json?" +
                "placeid=" + placeId + "&" +
                "fields=" + params + "&" +
                "key=" + API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String res_photo = null;
                        Double par_lat = null;
                        Double par_lng = null;

                        // khoi tao danh sach diem can phai ve
                        List<DataRestaurant> list = new ArrayList<>();

                        Gson gson = new Gson();
                        DataRestaurant data = gson.fromJson(response, DataRestaurant.class);

                        if (data != null) {
                            for (DataRestaurant.Result.Photos photo : data.result.photos) {
                                res_photo = photo.photo_reference;
                            }
                            par_lat = data.result.geometry.location.lat;
                            par_lng = data.result.geometry.location.lng;

                            Place place = new Place(data.result.place_id, null, data.result.name, data.result.formatted_address, data.result.formatted_phone_number, res_photo, par_lat, par_lng);
                            place.ratting = data.result.rating;
                            callback.result(place);
                        } else {
                            Log.d("Rest API", "no data");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Data from API: ", error.getMessage());

            }
        });

        queue.add(stringRequest);
        //endregion
    }

    //endregion

    public static ArrayList<Place> autocomplete(String input) {
        ArrayList<Place> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_AUTOCOMPLETE);
            sb.append(OUT_JSON);
            sb.append("?sensor=false");
            sb.append("&key=" + API_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<Place>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Place place = new Place();
                place.reference = predsJsonArray.getJSONObject(i).getString("reference");
                place.name = predsJsonArray.getJSONObject(i).getString("description");
                place.ratting = BigDecimal.valueOf(predsJsonArray.getJSONObject(i).getDouble("rating")).floatValue();
                resultList.add(place);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }

        return resultList;
    }

    //https://maps.googleapis.com/maps/api/place/search/json?sensor=false&key=AIzaSyB5EOs3RT36APJACuSjayKvJmUTTO9_1Fo&keyword=restaurant&location=16.06673,108.211981&radius=1000
    public static ArrayList<Place> search(String keyword, double lat, double lng, int radius) {
        ArrayList<Place> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_SEARCH);
            sb.append(OUT_JSON);
            sb.append("?sensor=false");
            sb.append("&key=" + API_KEY);
            sb.append("&keyword=" + URLEncoder.encode(keyword, "utf8"));
            sb.append("&location=" + String.valueOf(lat) + "," + String.valueOf(lng));
            sb.append("&radius=" + String.valueOf(radius));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("results");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<Place>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Place place = new Place();
                place.placeId = predsJsonArray.getJSONObject(i).getString("place_id");
                place.reference = predsJsonArray.getJSONObject(i).getString("reference");
                place.name = predsJsonArray.getJSONObject(i).getString("name");
                place.formatted_address = predsJsonArray.getJSONObject(i).getString("vicinity");
                place.ratting = BigDecimal.valueOf(predsJsonArray.getJSONObject(i).getDouble("rating")).floatValue();

                // Create a JSON object hierarchy from the results
                JSONObject predsGeoJsonObject = predsJsonArray.getJSONObject(i).getJSONObject("geometry");
                JSONObject predsLocationJsonObject = predsGeoJsonObject.getJSONObject("location");

                place.lat = predsLocationJsonObject.getDouble("lat");
                place.lng = predsLocationJsonObject.getDouble("lng");

                resultList.add(place);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }

        return resultList;
    }

    public static Place details(String reference) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE);
            sb.append(TYPE_DETAILS);
            sb.append(OUT_JSON);
            sb.append("?sensor=false");
            sb.append("&key=" + API_KEY);
            sb.append("&reference=" + URLEncoder.encode(reference, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        Place place = null;
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString()).getJSONObject("result");

            place = new Place();
            place.icon = jsonObj.getString("icon");
            place.name = jsonObj.getString("name");
            place.formatted_address = jsonObj.getString("formatted_address");
            if (jsonObj.has("formatted_phone_number")) {
                place.formatted_phone_number = jsonObj.getString("formatted_phone_number");
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON results", e);
        }

        return place;
    }
}
package com.example.nganth.restaurantapp.restaurant;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.nganth.restaurantapp.Place;
import com.example.nganth.restaurantapp.Restaurant;

import java.util.ArrayList;

public class PagerWalkthoughtAdapter extends FragmentStatePagerAdapter {

    //ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<Place> restaurants = new ArrayList<>();

    public PagerWalkthoughtAdapter(FragmentManager fm, ArrayList<Place> places) {
        super(fm);

        if(places.size() > 0){
            restaurants.clear();
            restaurants.addAll(places);
        }else{
            restaurants.add(new Place("dfsdf","https://nyoobserver.files.wordpress.com/2017/01/static1-squarespace-3.jpg", "abc", " New York, NY, USA","12344354","dsfsdf",0.0,0.0));
            restaurants.add(new Place("dfsdf","https://nyoobserver.files.wordpress.com/2017/01/static1-squarespace-3.jpg", "abc", " New York, NY, USA","12344354","dsfsdf",0.0,0.0));
            restaurants.add(new Place("dfsdf","https://nyoobserver.files.wordpress.com/2017/01/static1-squarespace-3.jpg", "abc", " New York, NY, USA","12344354","dsfsdf",0.0,0.0));
            restaurants.add(new Place("dfsdf","https://nyoobserver.files.wordpress.com/2017/01/static1-squarespace-3.jpg", "abc", " New York, NY, USA","12344354","dsfsdf",0.0,0.0));
            restaurants.add(new Place("dfsdf","https://nyoobserver.files.wordpress.com/2017/01/static1-squarespace-3.jpg", "abc", " New York, NY, USA","12344354","dsfsdf",0.0,0.0));
            restaurants.add(new Place("dfsdf","https://nyoobserver.files.wordpress.com/2017/01/static1-squarespace-3.jpg", "abc", " New York, NY, USA","12344354","dsfsdf",0.0,0.0));
            restaurants.add(new Place("dfsdf","https://nyoobserver.files.wordpress.com/2017/01/static1-squarespace-3.jpg", "abc", " New York, NY, USA","12344354","dsfsdf",0.0,0.0));
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ItemFragmentWalkthought();

        Bundle bundle = new Bundle();
        bundle.putSerializable("Res", restaurants.get(position));
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return restaurants.size();
    }
}

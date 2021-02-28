package com.example.nganth.restaurantapp.restaurant;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerMenuAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;

    public PagerMenuAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
        list.add(new MenuFragment());
        list.add(new AboutFragment());
        list.add(new ReviewFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Menu";
            case 1: return "About";
            case 2: return "Review";
        }
        return "";
    }
}

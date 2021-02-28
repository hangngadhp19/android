package com.example.nganth.restaurantapp.restaurant;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.databinding.ResultBinding;

public class ResultFragment extends Fragment {
    private ResultBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.result, container, false);

        RestaurantActivity mainActivity = (RestaurantActivity) getActivity();
        binding.setVariableResult(mainActivity);

        return binding.getRoot();
    }
}

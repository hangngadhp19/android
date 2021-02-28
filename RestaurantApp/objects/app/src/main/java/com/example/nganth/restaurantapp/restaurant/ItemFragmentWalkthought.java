package com.example.nganth.restaurantapp.restaurant;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nganth.restaurantapp.Place;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.Restaurant;
import com.example.nganth.restaurantapp.databinding.FragmentItemWalkthoughtBinding;

import java.util.ArrayList;

public class ItemFragmentWalkthought extends Fragment implements View.OnClickListener {

    private FragmentItemWalkthoughtBinding binding;

    private PagerWalkthoughtAdapter adapter;
    private Place data;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentItemWalkthoughtBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_walkthought, container, false);
        Bundle bundle = getArguments();
        String imgResPar = null;
        if (bundle != null) {
            data = (Place) bundle.getSerializable("Res");
            imgResPar = data.getIcon();

            String nameResPar = data.getName();
            binding.nameResWalkthought.setText(nameResPar);

            String addressResPar = data.getFormatted_address();
            binding.addressResWalkthought.setText(addressResPar);
        }
        if (TextUtils.isEmpty(imgResPar)) {
            binding.imgWalkthought.setImageResource(R.drawable.food_menu);
        } else {
            binding.imgWalkthought.setImageDrawable(Drawable.createFromPath(imgResPar));
        }

        binding.imgWalkthought.setOnClickListener(this);
        binding.nameResWalkthought.setOnClickListener(this);

        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(getContext(), ViewPagerMenuActivity.class);
        if (data != null) {
            intent.putExtra("place", data);
        }
        getActivity().startActivity(intent);
    }
}

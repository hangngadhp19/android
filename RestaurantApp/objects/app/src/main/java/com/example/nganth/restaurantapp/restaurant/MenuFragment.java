package com.example.nganth.restaurantapp.restaurant;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nganth.restaurantapp.Foods;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.databinding.MenuBinding;
import com.example.nganth.restaurantapp.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MenuFragment extends Fragment{

    private MenuBinding binding;
    ArrayList<Foods> foods = new ArrayList<>();
    private MenuAdapter adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference resRef = database.getReference("res_foods");
    DatabaseReference myRef = resRef.child("restaurant_1");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.menu, container, false);

        ViewPagerMenuActivity mainActivity = (ViewPagerMenuActivity) getActivity();
        binding.setVariableMenu(mainActivity);

        if (savedInstanceState == null) {
            //-- begin get data from firebase
            // Read from the database
            myRef.orderByChild("name").limitToLast(10).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    foods.clear();
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        Foods value = i.getValue(Foods.class);
                        value.setFoodId(i.getKey());

                        foods.add(value);
                    }

                    //-- order name asc
                    Collections.sort(foods, new Comparator<Foods>() {
                        @Override
                        public int compare(Foods foods, Foods t1) {
                            if (t1.getName() == null) return 0;
                            if (foods.getName() == null) return 1;
                            return foods.getName().compareTo(t1.getName());
                        }
                    });

                    adapter.notifyDataSetChanged();
                }



                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("restaurantapp", "Failed to read value.", error.toException());
                }
            });
            //-- end get data from firebase
        }


        // khoi tao Adapter
        adapter = new MenuAdapter(foods);

        // cung cap Adapter cho RecyclerView
        binding.lstResGrid.setAdapter(adapter);

        // thiet lap dang hien thi cho RecyclerView
        // hiển thị dạng lưới
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mainActivity.getApplicationContext(),
                2);
        binding.lstResGrid.setLayoutManager(gridLayoutManager);

        return binding.getRoot();
    }


}

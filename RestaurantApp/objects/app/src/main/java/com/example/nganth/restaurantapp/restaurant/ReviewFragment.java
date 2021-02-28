package com.example.nganth.restaurantapp.restaurant;



import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nganth.restaurantapp.Comments;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.databinding.ReviewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReviewFragment extends Fragment {
    private ReviewBinding binding;
    ArrayList<Comments> comments = new ArrayList<>();
    ReviewAdapter adapter;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    //-- lay bien place tu ViewPagerMenuActivity qua
    ViewPagerMenuActivity mainActivity;

    FirebaseDatabase database;
    DatabaseReference resRef;
    DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.review, container, false);

        mainActivity = (ViewPagerMenuActivity) getActivity();
        database = FirebaseDatabase.getInstance();
        resRef = database.getReference("res_comments");
        if (mainActivity.place != null)
        {
            myRef = resRef.child(mainActivity.place.getPlaceId());
        }

        ViewPagerMenuActivity mainActivity = (ViewPagerMenuActivity) getActivity();
        binding.setVariableReview(mainActivity);

        //-- begin get data from firebase
        // Read from the database
        if (myRef != null) {
            myRef.orderByChild(myRef.getKey()).limitToLast(10).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    comments.clear();
                    for (DataSnapshot i: dataSnapshot.getChildren()) {
                        Comments value = i.getValue(Comments.class);
                        value.setComment_id(i.getKey());

                        comments.add(value);
                    }

                    //-- order id desc
                    Collections.sort(comments, new Comparator<Comments>() {
                        @Override
                        public int compare(Comments comments, Comments t1) {
                            if (t1.getComment_id() == null) return 0;
                            if (comments.getComment_id() == null) return 1;
                            return t1.getComment_id().compareTo(comments.getComment_id());
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
        }
        //-- end get data from firebase


        // khoi tao Adapter
        adapter = new ReviewAdapter(comments);

        // cung cap Adapter cho RecyclerView
        binding.lstComment.setAdapter(adapter);

        // thiet lap dang hien thi cho RecyclerView
        // Hiển thị dạng danh sách
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity.getApplicationContext(),
                LinearLayoutManager.VERTICAL, // hiển thị theo chiều dọc
                /*LinearLayoutManager.HORIZONTAL,*/ // hiển thị theo chiều ngang
                false
        );
        binding.lstComment.setLayoutManager(linearLayoutManager);

        binding.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // Save data in to database
            Long id = date_time_now();
            DatabaseReference commentRef = myRef.child(Long.toString(id));

            // [START initialize_auth]
            mAuth = FirebaseAuth.getInstance();
            // [END initialize_auth]

            commentRef.child("comment").setValue(binding.txtInputComment.getText().toString());
            commentRef.child("mail").setValue(mAuth.getCurrentUser().getEmail());

            binding.txtInputComment.setText("");
            }
        });

        return binding.getRoot();
    }

    public static Long date_time_now() {
        Calendar calendar = Calendar.getInstance();
        Long id = calendar.getTime().getTime() / 1000;

        return id;
    }
}


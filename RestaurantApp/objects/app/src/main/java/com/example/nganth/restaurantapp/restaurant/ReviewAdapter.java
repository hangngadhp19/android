package com.example.nganth.restaurantapp.restaurant;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nganth.restaurantapp.Comments;
import com.example.nganth.restaurantapp.R;
import com.example.nganth.restaurantapp.databinding.ItemReviewBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter {
    private ArrayList<Comments> comments;


    public ReviewAdapter(ArrayList<Comments> comments) {
        this.comments = comments;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemReviewBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.findBinding(itemView);
            binding.txtReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    binding.txtComment.setText(comments.get(position).getComment());
                    binding.txtReadMore.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup recyclerView, int viewType) {
        // Lấy layout trong thư mục res
        // đối tượng hỗ trợ truy xuất tài nguyên layout trong thư mục res/layout
        LayoutInflater layoutInflater = LayoutInflater.from(recyclerView.getContext());
        // binding đến layout mà mình muốn lấy
        ItemReviewBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_review, recyclerView, false);
        // lấy layout ra
        View layout = binding.getRoot();

        // cung cấp cho ViewHolder
        MyViewHolder viewHolder = new MyViewHolder(layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String str_comment;

        ItemReviewBinding binding = DataBindingUtil.findBinding(holder.itemView);

        Comments data = comments.get(position);

        String date_format = convert_long_datetime(data.getComment_id());

        binding.txtDate.setText(date_format);
        binding.txtEmail.setText("- " + data.getMail());

        if (data.getComment().length() > 175) {
            str_comment = truncate(data.getComment(), 175);
            binding.txtReadMore.setVisibility(View.VISIBLE);
        } else {
            str_comment = data.getComment();
            binding.txtReadMore.setVisibility(View.INVISIBLE);
        }

        binding.txtComment.setText(str_comment);
    }

    public static String convert_long_datetime(String id) {
        long unixdate = Long.parseLong(id);
        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixdate * 1000);
        String date_format = formatter.format(calendar.getTime());

        return date_format;
    }


    public static String truncate(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len) + " ... ";
        } else {
            return str;
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}

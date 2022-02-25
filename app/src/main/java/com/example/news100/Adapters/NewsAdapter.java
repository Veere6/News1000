package com.example.news100.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.news100.Models.newsdata.Article;
import com.example.news100.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyHolder>{
    private final List<Article> dataList;
    private final Context context;

    public NewsAdapter(List<Article> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newscart_layout, parent, false);
        return new MyHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(dataList.get(position).getTitle());
        holder.discription.setText(dataList.get(position).getDescription());
        Glide.with(context).load(dataList.get(position).getUrlToImage())
                .placeholder(R.drawable.ic_image).into(holder.thumnail);
        holder.from.setText(dataList.get(position).getSource().getName());
        Instant c = Calendar.getInstance().getTime().toInstant();
        Instant instant1 = Instant.parse(c.toString());
        Instant instant2 = Instant.parse(dataList.get(position).getPublishedAt());
        Duration duration = Duration.between(instant1, instant2);
        System.out.println(duration.toDays());
        System.out.println(duration.toHours());
        System.out.println(duration.toMinutes());
        String times=duration.toHours()+"";
        holder.time.setText(times.replace("-","")+" Hours ago");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView title,discription,from,time;
        ImageView thumnail;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            discription=itemView.findViewById(R.id.discription);
            thumnail=itemView.findViewById(R.id.thumnail);
            from=itemView.findViewById(R.id.from);
            time=itemView.findViewById(R.id.time);
        }
    }
}

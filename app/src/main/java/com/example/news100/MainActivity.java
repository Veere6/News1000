package com.example.news100;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.news100.Adapters.NewsAdapter;
import com.example.news100.Models.newsdata.Article;
import com.example.news100.Models.newsdata.Data;
import com.example.news100.Services.ApiSource;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView news_Recycler;
    EditText searchBar;
    ProgressBar progressBar;
    TextView nodata,total_result;
    NewsAdapter newsAdapter;
    List<Article> dataList=new ArrayList<>();
    String currentdate;
    String total_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        searchBar=findViewById(R.id.searchBar);
        news_Recycler=findViewById(R.id.news_Recycler);
        progressBar=findViewById(R.id.progressBar);
        nodata=findViewById(R.id.nodata);
        total_result=findViewById(R.id.total_result);
        newsAdapter=new NewsAdapter(dataList,MainActivity.this);
        news_Recycler.setAdapter(newsAdapter);
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentdate = df.format(c);
        getNewsdata("indore");
        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    getNewsdata(s.toString());
                }
            }
        });
    }

    private void getNewsdata(String feed) {
        if (ApiSource.isConnectingToInternet(this)) {
            nodata.setVisibility(View.GONE);
            news_Recycler.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            dataList.clear();
            RequestQueue requstQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/everything?q="+feed+"&from="+currentdate+"&sortBy=publishedAt&apiKey=d89c7f26ee76480bb689199230a449cd" , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("NEWS APi => " +"https://newsapi.org/v2/everything?q="+feed+"&from="+currentdate+"&sortBy=publishedAt&apiKey=89c68de352f74d1ca14334cabb43597f");
                    System.out.println("NewsData response>>>> "+response.toString());
                    nodata.setVisibility(View.GONE);
                    news_Recycler.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    try{
                        Data data = new Gson().fromJson(response,Data.class);
                        total_count=data.getTotalResults().toString();
                        total_result.setText(total_count+" Results Found");
                        if (data.getStatus().equals("ok")){
                            if (data.getArticles().size()>0){
                                for (int i = 0; i < data.getArticles().size(); i++) {
                                    Article article = data.getArticles().get(i);
                                    dataList.add(article);
                                    newsAdapter.notifyDataSetChanged();
                                }
                            }else{
                                nodata.setText("No News Found Today");
                                nodata.setVisibility(View.VISIBLE);
                                news_Recycler.setVisibility(View.GONE);
                                progressBar.setVisibility(View.GONE);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        nodata.setText(e.toString());
                        nodata.setVisibility(View.VISIBLE);
                        news_Recycler.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    nodata.setText(error.toString());
                    nodata.setVisibility(View.VISIBLE);
                    news_Recycler.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("User-Agent", "Mozilla/5.0");
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requstQueue.add(stringRequest);
        }else {
            nodata.setVisibility(View.VISIBLE);
            news_Recycler.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
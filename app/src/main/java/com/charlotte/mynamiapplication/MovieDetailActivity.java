package com.charlotte.mynamiapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/2/23 0023.
 */
public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity_detail);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.detail_container,new MovieDetailFragment()).commit();
        }
    }
}

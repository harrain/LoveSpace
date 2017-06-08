package com.example.lovespace.main.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lovespace.R;

/**
 * Created by stephen on 2017/6/6.
 */

public class BaseActivity extends AppCompatActivity {


    public ImageButton back;
    public ImageButton add;
    public TextView title;
    public Toolbar toolbar;
    public Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        back = (ImageButton) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        add = (ImageButton) findViewById(R.id.add);
        setSupportActionBar(toolbar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

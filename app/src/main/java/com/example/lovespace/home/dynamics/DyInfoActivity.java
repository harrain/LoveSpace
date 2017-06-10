package com.example.lovespace.home.dynamics;

import android.os.Bundle;

import com.example.lovespace.R;
import com.example.lovespace.main.activity.BaseActivity;

public class DyInfoActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dy_info);
        super.onCreate(savedInstanceState);
        title.setText("留言详情");

    }
}

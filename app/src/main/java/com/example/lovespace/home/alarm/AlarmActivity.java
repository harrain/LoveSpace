package com.example.lovespace.home.alarm;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.lovespace.R;
import com.example.lovespace.home.SpaceItemDecoration;
import com.example.lovespace.main.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmActivity extends BaseActivity {


    @BindView(R.id.alarm_rv)
    RecyclerView alarmRv;
    @BindView(R.id.check_btn)
    FloatingActionButton checkBtn;
    private AlarmAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        title.setText("闹钟");
        initRecyclerView();

    }

    private void initRecyclerView() {
        adapter = new AlarmAdapter(mContext,null);
        alarmRv.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        alarmRv.setLayoutManager(layoutManager);
        alarmRv.addItemDecoration(new SpaceItemDecoration(2));

    }


    @OnClick(R.id.check_btn)
    public void check() {
    }
}

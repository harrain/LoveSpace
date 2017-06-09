package com.example.lovespace.home.alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.home.SpaceItemDecoration;
import com.example.lovespace.main.activity.BaseActivity;
import com.example.lovespace.main.model.bean.Alarm;
import com.example.lovespace.main.model.dao.AlarmDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class AlarmActivity extends BaseActivity {


    @BindView(R.id.alarm_rv)
    RecyclerView alarmRv;
    @BindView(R.id.check_btn)
    FloatingActionButton checkBtn;
    @BindView(R.id.alarm_pb)
    ProgressBar alarmPb;

    private AlarmAdapter adapter;
    Context mContext;
    private List<Alarm> alarms;

    private String uid;
    private String cid;
    private String TAG = "AlarmActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mContext = this;
        title.setText("闹钟");
        initRecyclerView();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlarmActivity.this,CreateAlarmActivity.class));
            }
        });

    }

    private void initRecyclerView() {
        adapter = new AlarmAdapter(mContext,alarms);
        alarmRv.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        alarmRv.setLayoutManager(layoutManager);
        alarmRv.addItemDecoration(new SpaceItemDecoration(2));

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        alarms.clear();
        fetchData();
    }

    private void fetchData() {
        AlarmDao.fetchAlarms(uid, cid, new SQLQueryListener<Alarm>() {
            @Override
            public void done(BmobQueryResult<Alarm> result, BmobException e) {
                if(e ==null){
                    List<Alarm> list = result.getResults();
                    if(list!=null && list.size()>0){
                        alarms.addAll(list);

                        adapter.notifyDataSetChanged();
                    }else{
                        Log.e(TAG, "查询成功，无数据返回");

                    }
                }else{
                    Log.e(TAG, "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());

                }
                alarmPb.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initData() {
        uid = Preferences.getUserId();
        cid = Preferences.getCoupleId();
        fetchData();

    }


    @OnClick(R.id.check_btn)
    public void check() {
    }
}

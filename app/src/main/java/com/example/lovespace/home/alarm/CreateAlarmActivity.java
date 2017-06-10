package com.example.lovespace.home.alarm;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.main.activity.BaseActivity;
import com.example.lovespace.main.model.dao.AlarmDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class CreateAlarmActivity extends BaseActivity {

    @BindView(R.id.aname_et)
    EditText anameEt;
    @BindView(R.id.time_picker)
    RelativeLayout timePicker;
    @BindView(R.id.tt)
    TextView tt;
    final int DATE_DIALOG = 1;
    @BindView(R.id.sync_alarm_cb)
    AppCompatCheckBox syncAlarmCb;
    private int mHour;
    private int mMinute;
    private int count;

    private String TAG = "CreateAlarmActivity";
    private String cid;
    private String uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_alarm);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        add.setImageResource(R.drawable.done_icon);
        final Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name != null){
            String time = intent.getStringExtra("time");
            title.setText("闹钟详情");
            anameEt.setText(name);
            tt.setText(time);
            syncAlarmCb.setChecked(intent.getBooleanExtra("sync",false));
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkSync();
                    AlarmDao.updateAlarm(anameEt.getText().toString(), timeFormat(), uid, cid, intent.getStringExtra("objectId"),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "更新闹钟成功", Toast.LENGTH_SHORT).show();
                                cancelAlarm(intent.getIntExtra("index",0));
                                go2AlarmActivity();
                                startAlarmService();
                                finish();
                            }else {
                                Log.e(TAG, "error:" + e.getMessage());
                            }
                        }
                    });
                }
            });
            return;
        }
        title.setText("创建闹钟");
        count = intent.getIntExtra("alarmcount",0);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSync();
                AlarmDao.addToBmob(anameEt.getText().toString(), timeFormat(), uid, cid, new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            go2AlarmActivity();
                            startAlarmService();
                            finish();
                        } else {
                            Log.e(TAG, "error:" + e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void checkSync() {
        cid = null;
        uid = null;
        if (syncAlarmCb.isChecked()){
            cid = Preferences.getCoupleId();
        }else {
            uid = Preferences.getUserId();
        }
    }

    private void cancelAlarm(int position){
        Intent intent1 = new Intent(mContext, AlarmReceiver.class);
        intent1.putExtra("alarm_type", position + "");
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, position, intent1, 0);
        manager.cancel(pi);
    }


    private void startAlarmService() {
        Intent i = new Intent(CreateAlarmActivity.this,AlarmService.class);
        i .putExtra("time",timeFormat());
        i.putExtra("alarmCount",count++);
        startService(i);
    }

    private void go2AlarmActivity() {
        Intent intent = new Intent(CreateAlarmActivity.this, AlarmActivity.class);

        startActivity(intent);
    }

    @OnClick(R.id.time_picker)
    public void onTimePick() {
        showDialog(DATE_DIALOG);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new TimePickerDialog(this, mTimeListener, mHour, mMinute, true);
        }
        return null;
    }


    private TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            Log.e(TAG, timeFormat());
            tt.setText(timeFormat());
        }
    };

    public String timeFormat(){
        StringBuilder sb = new StringBuilder();
        if (mHour < 10){
            sb.append("0");
            sb.append(mHour);
        }else {
            sb.append(mHour);
        }
        sb.append("-");
        if (mMinute <10){
            sb.append("0");
            sb.append(mMinute);
        }else {
            sb.append(mMinute);
        }
        return sb.toString();
    }
}

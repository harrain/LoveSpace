package com.example.lovespace.home.alarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.home.annversary.AnnversaryActivity;
import com.example.lovespace.home.annversary.CreateAnniActivity;
import com.example.lovespace.main.activity.BaseActivity;
import com.example.lovespace.main.model.dao.AlarmDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CreateAlarmActivity extends BaseActivity {

    @BindView(R.id.aname_et)
    EditText anameEt;
    @BindView(R.id.time_picker)
    RelativeLayout timePicker;
    @BindView(R.id.tt)
    TextView tt;
    final int DATE_DIALOG = 1;
    private int mHour;
    private int mMinute;

    private String TAG = "CreateAlarmActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_alarm);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        title.setText("创建闹钟");
        add.setImageResource(R.drawable.done_icon);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmDao.addToBmob(anameEt.getText().toString(), mHour + ":" + mMinute, null, Preferences.getCoupleId(), new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Intent intent = new Intent(CreateAlarmActivity.this, AlarmActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Log.e(TAG,"error:"+e.getMessage());
                        }
                    }
                });
            }
        });
    }

    @OnClick(R.id.time_picker)
    public void onTimePick() {
        showDialog(DATE_DIALOG);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new TimePickerDialog(this, mTimeListener, mHour, mMinute,true);
        }
        return null;
    }


    private TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            Log.e(TAG,mHour+":"+mMinute);
            tt.setText(mHour+":"+mMinute);
        }
    };
}

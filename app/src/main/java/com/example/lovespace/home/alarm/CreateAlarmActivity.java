package com.example.lovespace.home.alarm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.example.lovespace.R;
import com.example.lovespace.main.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAlarmActivity extends BaseActivity {

    @BindView(R.id.aname_et)
    EditText anameEt;
    @BindView(R.id.time_picker)
    RelativeLayout timePicker;
    final int DATE_DIALOG = 1;
    private int mHour;
    private int mMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_alarm);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        title.setText("创建闹钟");
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
        }
    };
}

package com.example.lovespace.home.annversary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.main.model.dao.AnniDao;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CreateAnniActivity extends AppCompatActivity {

    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add)
    ImageButton add;
    @BindView(R.id.aname_et)
    EditText anameEt;
    @BindView(R.id.date_picker)
    RelativeLayout datePicker;
    @BindView(R.id.dt)
    TextView dt;
    private static String TAG = "CreateAnniActivity";
    private Context mContext;

    final int DATE_DIALOG = 1;
    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_anni);
        ButterKnife.bind(this);
        mContext = this;
        add.setImageResource(R.drawable.done_icon);
        title.setText("创建纪念日");

        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }


    @OnClick(R.id.back)
    public void onBackClicked() {
        finish();
    }

    @OnClick(R.id.add)
    public void onAddClicked() {
        AnniDao.addToBmob(anameEt.getText().toString(), mYear+"-"+mMonth+"-"+mDay, null, null, Preferences.getCoupleId(), new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Intent intent = new Intent(CreateAnniActivity.this, AnnversaryActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    Log.e(TAG,"error:"+e.getMessage());
                }
            }
        });
    }

    @OnClick(R.id.date_picker)
    public void onDatePickerClicked() {
        showDialog(DATE_DIALOG);
        /*AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext,R.layout.date_picker_view,null);
        builder.setView(view);
        Button cancel = (Button) view.findViewById(R.id.cancel_b);
        Button set = (Button) view.findViewById(R.id.set_b);
        AlertDialog dialog = builder.create();
        dialog.show();*/
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            Log.e(TAG,mYear+"-"+mMonth+"-"+mDay);
            dt.setText(mYear+"-"+mMonth+"-"+mDay);
        }
    };



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClicked();
    }
}

package com.example.lovespace.home.annversary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.main.model.dao.AnniDao;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
        final Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name != null){
            String time = intent.getStringExtra("time");
            title.setText("纪念日详情");
            anameEt.setText(name);
            dt.setText(time);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AnniDao.updateAnni(anameEt.getText().toString(), dateFormat(),
                            Preferences.getCoupleId(), intent.getStringExtra("objectId"), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(mContext, "更新纪念日成功", Toast.LENGTH_SHORT).show();
                                        go2AnniActivity();
                                        finish();
                                    }else {
                                        if (e.getErrorCode() == 9010){
                                            Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                                        }else if (e.getErrorCode() == 9016){
                                            Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                                        }
                                        Log.e(TAG,"updateerror:"+e.getMessage());
                                    }
                                }
                            });
                }
            });
            return;
        }
        String footname = intent.getStringExtra("footname");
        anameEt.setText(footname);
        title.setText("创建纪念日");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setEnabled(false);
                onAdd();
            }
        });
    }


    @OnClick(R.id.back)
    public void onBackClicked() {
        finish();
    }


    public void onAdd() {
        AnniDao.addToBmob(anameEt.getText().toString(), dateFormat(), null, null, Preferences.getCoupleId(), new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    go2AnniActivity();
                    finish();
                }else {
                    if (e.getErrorCode() == 9010){
                        Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                    }else if (e.getErrorCode() == 9016){
                        Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                    }
                    Log.e(TAG,"adderror:"+e.getMessage());
                    add.setEnabled(true);
                }
            }
        });
    }

    private void go2AnniActivity() {
        Intent intent = new Intent(CreateAnniActivity.this, AnnversaryActivity.class);
        startActivity(intent);
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
            mMonth = monthOfYear +1;
            mDay = dayOfMonth;
            Log.e(TAG,dateFormat());
            dt.setText(dateFormat());
        }
    };



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClicked();
    }

    public String dateFormat(){
        StringBuilder sb = new StringBuilder();
        sb.append(mYear);
        sb.append("-");
        if (mMonth < 10){
            sb.append("0");
            sb.append(mMonth);
        }else {
            sb.append(mMonth);
        }
        sb.append("-");
        if (mDay <10){
            sb.append("0");
            sb.append(mDay);
        }else {
            sb.append(mDay);
        }
        return sb.toString();
    }
}

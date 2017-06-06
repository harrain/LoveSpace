package com.example.lovespace.home.annversary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.main.model.dao.AnniDao;

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
    private static String TAG = "CreateAnniActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_anni);
        ButterKnife.bind(this);
        mContext = this;
        add.setImageResource(R.drawable.done_icon);
        title.setText("创建纪念日");
    }


    @OnClick(R.id.back)
    public void onBackClicked() {
        finish();
    }

    @OnClick(R.id.add)
    public void onAddClicked() {
        AnniDao.addToBmob(anameEt.getText().toString(), null, null, null, Preferences.getCoupleId(), new SaveListener<String>() {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext,R.layout.date_picker_view,null);
        builder.setView(view);
        Button cancel = (Button) view.findViewById(R.id.cancel_b);
        Button set = (Button) view.findViewById(R.id.set_b);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClicked();
    }
}

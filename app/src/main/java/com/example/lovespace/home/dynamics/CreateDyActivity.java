package com.example.lovespace.home.dynamics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.main.activity.BaseActivity;
import com.example.lovespace.main.model.dao.DyDao;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CreateDyActivity extends BaseActivity {

    @BindView(R.id.dy_content_et)
    EditText dyContentEt;
    @BindView(R.id.cdy_pb)
    ProgressBar cdyPb;
    private String TAG = "CreateDyActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_dy);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        title.setText("写留言");
        add.setImageResource(R.drawable.done_icon);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setEnabled(false);
                cdyPb.setVisibility(View.VISIBLE);
                Log.e(TAG,"uid:"+Preferences.getUserId());
                DyDao.addDy(dyContentEt.getText().toString(), Preferences.getUserId(),
                        Preferences.getCoupleId()
                        , new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {

                                    Toast.makeText(mContext, "发布留言成功", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mContext,DynamicsActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if (e.getErrorCode() == 9010){
                                        Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                                    }else if (e.getErrorCode() == 9016){
                                        Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(mContext, "发布留言失败", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "e:" + e.getMessage());
                                }
                                cdyPb.setVisibility(View.INVISIBLE);
                                add.setEnabled(true);
                            }
                        });
            }
        });
    }
}

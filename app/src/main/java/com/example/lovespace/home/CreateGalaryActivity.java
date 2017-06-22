package com.example.lovespace.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.main.model.bean.Galary;
import com.example.lovespace.main.model.dao.GalaryDao;
import com.netease.nim.uikit.common.activity.UI;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;

public class CreateGalaryActivity extends UI {

    @BindView(R.id.left)
    TextView left;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.gname_et)
    EditText gnameEt;
    @BindView(R.id.create_galary_pb)
    ProgressBar cPb;

    private String cid;
    private String TAG = "CreateGalaryActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_galary);
        ButterKnife.bind(this);
        mContext = this;
        setSupportActionBar(toolbar);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right.setEnabled(false);
                String name = gnameEt.getText().toString();
                cPb.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(cid)){
                    Log.e(TAG,"cid为空");
                    Toast.makeText(mContext, "cid为空,无法创建相册", Toast.LENGTH_SHORT).show();
                    right.setEnabled(true);
                    return;
                }
                GalaryDao.addToBomb(name, cid, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean b) {
                                cPb.setVisibility(View.INVISIBLE);
                                Toast.makeText(CreateGalaryActivity.this, "创建相册成功", Toast.LENGTH_SHORT).show();
                                onSuccessDone();
                        right.setEnabled(true);
                    }

                    @Override
                    public void onFailure(BmobException e) {
                        cPb.setVisibility(View.INVISIBLE);
                        Toast.makeText(CreateGalaryActivity.this, "创建相册失败", Toast.LENGTH_SHORT).show();
                        if (e.getErrorCode() == 9010){
                            Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                        }else if (e.getErrorCode() == 9016){
                            Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                        }
                        right.setEnabled(true);
                    }
                });

            }
        });

        gnameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                GalaryDao.queryGalaryInfo(s.toString(), cid, new OnCompleteListener<Galary>() {
                    @Override
                    public void onSuccess(Galary data) {
                        Toast.makeText(CreateGalaryActivity.this, "该相册名已存在", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(BmobException e) {
                        if (e.getErrorCode() == 9010){
                            Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                        }else if (e.getErrorCode() == 9016){
                            Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                        }
                        Log.e(TAG,"queryGalaryInfo:"+e.getMessage());
                    }
                });

            }
        });
        cid = Preferences.getCoupleId();

    }

    private void onSuccessDone(){
        Intent intent = new Intent(CreateGalaryActivity.this,GalaryActivity.class);
        startActivity(intent);
        finish();
    }


}

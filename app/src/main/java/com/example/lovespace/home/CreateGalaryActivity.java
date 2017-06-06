package com.example.lovespace.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_galary);
        ButterKnife.bind(this);

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
                String name = gnameEt.getText().toString();
                cPb.setVisibility(View.VISIBLE);
                GalaryDao.addToBomb(name, cid, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean b) {

                                cPb.setVisibility(View.INVISIBLE);
                                Toast.makeText(CreateGalaryActivity.this, "创建相册成功", Toast.LENGTH_SHORT).show();
                                onSuccessDone();

                    }

                    @Override
                    public void onFailure(Exception e) {
                        cPb.setVisibility(View.INVISIBLE);
                        Toast.makeText(CreateGalaryActivity.this, "创建相册失败", Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Exception e) {

                    }
                });

            }
        });
        cid = Preferences.getCoupleId();

    }

    private void onSuccessDone(){
        Intent intent = new Intent(CreateGalaryActivity.this,GalaryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}

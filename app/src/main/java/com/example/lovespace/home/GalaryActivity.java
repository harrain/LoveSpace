package com.example.lovespace.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.main.model.bean.Galary;
import com.example.lovespace.main.model.dao.GalaryDao;
import com.netease.nim.uikit.common.activity.UI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalaryActivity extends UI {

    @BindView(R.id.create_galary_btn)
    ImageButton createGalaryBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.galary_num_tv)
    TextView galaryNumTv;
    @BindView(R.id.galary_rl)
    RecyclerView galaryRl;
    @BindView(R.id.galary_pb)
    ProgressBar progressBar;

    private List<Galary> galaries;
    private GalaryAdapter adapter;
    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_galary);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        galaries = new ArrayList<>();

        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        initview();
        initData();
    }

    private void initData() {
        cid = Preferences.getCoupleId();
        refreshUI();

    }

    private void refreshUI(){

            GalaryDao.fetchGalarys(cid, new OnCompleteListener<List<Galary>>() {
                @Override
                public void onSuccess(List<Galary> data) {
                    galaries.addAll(data);
                    progressBar.setVisibility(View.INVISIBLE);
                    galaryNumTv.setText("相册| "+galaries.size());
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

    }

    public void initview(){
        adapter = new GalaryAdapter(getmContext(),galaries);
        galaryRl.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getmContext(),2);

        galaryRl.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        galaryRl.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    @OnClick(R.id.create_galary_btn)
    public void createGalary(){
        startActivity(new Intent(this,CreateGalaryActivity.class));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        galaries.clear();
        refreshUI();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

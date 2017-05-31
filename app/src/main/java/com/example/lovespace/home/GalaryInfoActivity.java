package com.example.lovespace.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lovespace.R;
import com.netease.nim.uikit.common.activity.UI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalaryInfoActivity extends UI {

    @BindView(R.id.more_set_btn)
    ImageButton moreSetBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.gi_title_tv)
    TextView giTitleTv;
    @BindView(R.id.subtext)
    TextView subtext;
    @BindView(R.id.upload_pic_btn)
    Button uploadPicBtn;
    @BindView(R.id.galaryinfo_rv)
    RecyclerView galaryinfoRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galary_info);
        ButterKnife.bind(this);

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
    }

    public void initview(){
        GalaryInfoAdapter adapter = new GalaryInfoAdapter(getmContext());
        galaryinfoRv.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getmContext(),2);
        galaryinfoRv.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        galaryinfoRv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    @OnClick(R.id.upload_pic_btn)
    public void uploadImage(){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

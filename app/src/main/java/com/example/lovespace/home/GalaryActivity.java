package com.example.lovespace.home;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lovespace.R;
import com.netease.nim.uikit.common.activity.UI;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalaryActivity extends UI {

    @BindView(R.id.create_galary_btn)
    ImageButton createGalaryBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.galary_num_tv)
    TextView galaryNumTv;
    @BindView(R.id.galary_rl)
    RecyclerView galaryRl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_galary);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

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
        GalaryAdapter adapter = new GalaryAdapter(getmContext());
        galaryRl.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getmContext(),2);

        galaryRl.setLayoutManager(layoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        galaryRl.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

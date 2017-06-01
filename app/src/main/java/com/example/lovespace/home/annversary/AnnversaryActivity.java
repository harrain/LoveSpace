package com.example.lovespace.home.annversary;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.example.lovespace.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnnversaryActivity extends AppCompatActivity {

    @BindView(R.id.anni_add_btn)
    ImageButton anniAddBtn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.anni_rv)
    RecyclerView anniRv;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annversary);
        ButterKnife.bind(this);

        mContext = this;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);

    }

    @OnClick(R.id.anni_add_btn)
    public void addAnni() {
    }
}

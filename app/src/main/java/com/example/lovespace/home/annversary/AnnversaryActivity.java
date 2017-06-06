package com.example.lovespace.home.annversary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.home.SpaceItemDecoration;
import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.main.model.bean.Anni;
import com.example.lovespace.main.model.dao.AnniDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnnversaryActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.anni_rv)
    RecyclerView anniRv;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.add)
    ImageButton add;
    @BindView(R.id.anni_pb)
    ProgressBar anniPb;

    Context mContext;
    private List<Anni> annis;
    private AnniAdapter adapter;
    private String uid;
    private String cid;

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
        annis = new ArrayList<>();
        initRecyclerView();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        annis.clear();
        fetchData();
    }

    private void initData() {
        uid = Preferences.getUserId();
        cid = Preferences.getCoupleId();
        fetchData();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        anniRv.setLayoutManager(layoutManager);
        adapter = new AnniAdapter(mContext, annis);
        anniRv.setAdapter(adapter);

        anniRv.addItemDecoration(new SpaceItemDecoration(10));

    }

    private void fetchData(){
        AnniDao.fetchAnnis(uid, cid, new OnCompleteListener<List<Anni>>() {
            @Override
            public void onSuccess(List<Anni> data) {
                if (data.size()>0) {
                    annis.addAll(data);
                    anniPb.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                }
                anniPb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Exception e) {
                anniPb.setVisibility(View.INVISIBLE);
            }
        });
    }

    @OnClick(R.id.add)
    public void addAnni() {
        startActivity(new Intent(this,CreateAnniActivity.class));
    }

    @OnClick(R.id.back)
    public void backPressed() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed();
    }
}

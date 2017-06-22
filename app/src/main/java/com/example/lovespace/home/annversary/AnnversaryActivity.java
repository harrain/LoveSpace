package com.example.lovespace.home.annversary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

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
    int index;
    private String TAG = "AnnversaryActivity";

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
        Log.e(TAG,"onNewIntent...");
        anniPb.setVisibility(View.VISIBLE);
        fetchData();
    }

    private void initData() {
        uid = Preferences.getUserId();
        cid = Preferences.getCoupleId();
        anniPb.setVisibility(View.VISIBLE);
        fetchData();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        anniRv.setLayoutManager(layoutManager);
        adapter = new AnniAdapter(mContext, annis);
        anniRv.setAdapter(adapter);
        anniRv.addItemDecoration(new SpaceItemDecoration(10));

        //注册视图对象，即为ListView控件注册上下文菜单
        registerForContextMenu(anniRv);

        adapter.setOnItemClickListener(new AnniAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(mContext,CreateAnniActivity.class);
                i.putExtra("name",annis.get(position).getAnniname());
                i.putExtra("time",annis.get(position).getAnnidate());
                i.putExtra("objectId",annis.get(position).getObjectId());
                i.putExtra("index",position);
                startActivity(i);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.e(TAG,"u click the "+ position+" ..");
                index = position;
            }
        });

    }

    private void fetchData(){
        AnniDao.fetchAnnis(cid, new OnCompleteListener<List<Anni>>() {
            @Override
            public void onSuccess(List<Anni> data) {
                if (data.size()>0) {
                    annis.clear();
                    annis.addAll(data);
                    Log.e(TAG,"refresh size:"+annis.size());
                    anniPb.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                }
                anniPb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(BmobException e) {

                anniPb.setVisibility(View.INVISIBLE);
                if (e.getErrorCode() == 9010){
                    Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                }else if (e.getErrorCode() == 9016){
                    Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG,"fetchannis:"+e.getMessage());
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.amenu,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete){
            AnniDao.deleteRow(annis.get(index).getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null){
                        annis.remove(index);
                        adapter.notifyItemRemoved(index);
                        adapter.notifyDataSetChanged();
                    }else {
                        if (e.getErrorCode() == 9010){
                            Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                        }else if (e.getErrorCode() == 9016){
                            Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                        }
                        Log.e(TAG,"deleteexception:"+e.getMessage());
                    }
                }
            });
        }
        return super.onContextItemSelected(item);
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

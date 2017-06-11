package com.example.lovespace.home.dynamics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.main.activity.BaseActivity;
import com.example.lovespace.main.model.bean.Commit;
import com.example.lovespace.main.model.bean.Dynamics;
import com.example.lovespace.main.model.dao.DyDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class DynamicsActivity extends BaseActivity {

    Context mContext;
    @BindView(R.id.dy_rv)
    RecyclerView dyRv;

    @BindView(R.id.dy_pb)
    ProgressBar dyPb;
    private List<Dynamics> dys;
    private String TAG = "DynamicsActivity";
    private DynamicsAdapter adapter;
    int index;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dynamics);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        title.setText("留言板");
        mContext = this;
        dys = new ArrayList<>();
        initRecyclerView();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,CreateDyActivity.class));
            }
        });
        initData();

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        dyRv.setLayoutManager(layoutManager);
        adapter = new DynamicsAdapter(this,dys);
        dyRv.setAdapter(adapter);
        //注册视图对象，即为RecyclerView控件注册上下文菜单
        registerForContextMenu(dyRv);

        adapter.setOnItemClickListener(new DynamicsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                index = position;
                Log.e(TAG,"U click "+position+" ..");
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
            delete();
        }
        return super.onContextItemSelected(item);
    }

    private void delete(){
        DyDao.fetchCommit(Preferences.getCoupleId(), dys.get(index).getObjectId(), new SQLQueryListener<Commit>() {
            @Override
            public void done(BmobQueryResult<Commit> result, BmobException e) {
                if (e==null){
                    List<Commit> list = result.getResults();
                    if (list == null || list.size() == 0){
                        deleteDy();
                        return;
                    }
                    List<BmobObject> o = new ArrayList<BmobObject>();
                    for (Commit commit : list) {
                        Commit c = new Commit();
                        c.setObjectId(commit.getObjectId());
                        o.add(c);
                    }
                    DyDao.deleteCommits(o, new QueryListListener<BatchResult>() {
                        @Override
                        public void done(List<BatchResult> list, BmobException e) {
                            if(e==null){
                                deleteDy();
                            }else{
                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }else {
                    if (e.getErrorCode() == 9010){
                        Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                    }else if (e.getErrorCode() == 9016){
                        Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                    }
                    Log.e(TAG,"fetchCommit:"+e.getMessage());
                }
            }
        });
    }

    private void deleteDy(){
        DyDao.deleteDy(dys.get(index).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(mContext, "删除留言成功", Toast.LENGTH_SHORT).show();
                    dys.remove(index);
                    adapter.notifyItemRemoved(index);
                    adapter.notifyDataSetChanged();
                }else {
                    if (e.getErrorCode() == 9010){
                        Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                    }else if (e.getErrorCode() == 9016){
                        Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(mContext, "删除留言失败", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"exception:"+e.getMessage());
                }

            }
        });
    }

    private void initData() {
        DyDao.fetchDy(Preferences.getCoupleId(), new SQLQueryListener<Dynamics>() {
            @Override
            public void done(BmobQueryResult<Dynamics> result, BmobException e) {
                if (e == null){
                    List<Dynamics> list = result.getResults();
                    if(list!=null && list.size()>0){
                        dys.clear();
                        dys.addAll(list);
                        adapter.notifyDataSetChanged();
                        Log.e(TAG,"list size:"+dys.size());
                    }else{
                        Log.e(TAG, "查询成功，无数据返回");
                    }
                }else{
                    if (e.getErrorCode() == 9010){
                        Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                    }else if (e.getErrorCode() == 9016){
                        Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                    }
                    Log.e(TAG, "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
                dyPb.setVisibility(View.INVISIBLE);


            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG,"onNewIntent");
        dyPb.setVisibility(View.VISIBLE);
        initData();
    }
}

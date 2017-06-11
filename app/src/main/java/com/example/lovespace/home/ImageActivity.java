package com.example.lovespace.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lovespace.R;
import com.example.lovespace.common.util.OkHttpUtils;
import com.example.lovespace.main.model.dao.ImageDao;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ImageActivity extends AppCompatActivity {

    @BindView(R.id.ima_back)
    ImageButton imaBack;
    @BindView(R.id.ima_delete)
    ImageButton imaDelete;
    @BindView(R.id.ima_download)
    ImageButton imaDown;
    @BindView(R.id.ima_iv)
    ImageView imaIv;
    @BindView(R.id.ima_pb)
    ProgressBar imaPb;
    private String objectId;
    private String TAG = "ImageActivity";
    Context mContext;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        mContext = this;

        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
        imagePath = intent.getStringExtra("imagepath");
        Glide.with(this).load(imagePath).into(imaIv);
        imaPb.setVisibility(View.INVISIBLE);

    }

    @OnClick(R.id.ima_back)
    public void onImaBackClicked() {
        finish();
    }

    @OnClick(R.id.ima_download)
    public void onDownload(){
        imaPb.setVisibility(View.VISIBLE);
        final OkHttpUtils<Object> okHttpUtils = new OkHttpUtils(mContext);
        okHttpUtils.setRequestUrl(imagePath)
                .doInBackground(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        long timmillis = SystemClock.elapsedRealtime();
                        File file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),timmillis+".jpg");

                        Log.e(TAG,"imagelocal:"+file.getAbsolutePath());
                        try {
                            okHttpUtils.downloadFile(response,file);
                            Toast.makeText(mContext, "成功保存到"+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                            imaPb.setVisibility(View.INVISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .execute(new OkHttpUtils.OnCompleteListener<Object>() {
                    @Override
                    public void onSuccess(Object result) {
                        Toast.makeText(mContext, "成功保存到相册", Toast.LENGTH_SHORT).show();
                        imaPb.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(mContext, "保存失败", Toast.LENGTH_SHORT).show();
                        Log.e(TAG,"saveimage:"+error);
                        imaPb.setVisibility(View.INVISIBLE);
                    }
                });

    }

    @OnClick(R.id.ima_delete)
    public void onImaDeleteClicked() {
        ImageDao.deleteRow(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Toast.makeText(ImageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(mContext,GalaryInfoActivity.class));
                    finish();
                }else{
                    Log.e(TAG,"deleteimage:"+e.getMessage());
                }

            }
        });
    }


}

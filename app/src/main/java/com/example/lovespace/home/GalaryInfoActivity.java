package com.example.lovespace.home;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lovespace.R;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.main.model.bean.Galary;
import com.example.lovespace.main.model.bean.Image;
import com.example.lovespace.main.model.dao.GalaryDao;
import com.example.lovespace.main.model.dao.ImageDao;
import com.netease.nim.uikit.common.activity.UI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

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
    @BindView(R.id.image_pb)
    ProgressBar imagePb;

    //调用系统相册-选择图片
    private static final int CHOOSE_PHOTO = 1;
    private List<String> imagePaths;
    private GalaryInfoAdapter adapter;
    private static  final String TAG = "GalaryInfoActivity";
    private String gid;
    private String gname;
    private int imageCount;
    private String cid;
    private String gtime;
    private int isum;
    private List<Image> images;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galary_info);
        ButterKnife.bind(this);
        mContext = this;

        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        imagePaths = new ArrayList<>();
        images = new ArrayList<>();
        initRecyclerview();
        Intent intent = getIntent();
        gid = intent.getStringExtra("gid");
        gname = intent.getStringExtra("gname");
        gtime = intent.getStringExtra("gtime");
        isum = intent.getIntExtra("isum",0);
        setUpView();
        registerForContextMenu(moreSetBtn);
        cid = Preferences.getCoupleId();
        if (TextUtils.isEmpty(cid)){
            Log.e(TAG,"cid为空");
            Toast.makeText(mContext, "cid为空", Toast.LENGTH_SHORT).show();
        }
        loadData();
    }

    private void setUpView() {
        giTitleTv.setText(gname);
        subtext.setText(gtime+"-"+isum+"枚");
        imageCount = isum;
    }

    private void loadData() {
        ImageDao.queryImagesInfo(gid, cid, new OnCompleteListener<List<Image>>() {
            @Override
            public void onSuccess(List<Image> data) {
                images.clear();
                images.addAll(data);
                for (Image image:data) {
                    imagePaths.add(image.getImageurl());
                }
                if (imagePaths.size()> 0) {

                    adapter.notifyDataSetChanged();
                }
                imagePb.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(BmobException e) {
                if (e.getErrorCode() == 9010){
                    Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                }else if (e.getErrorCode() == 9016){
                    Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                }
                imagePb.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void initRecyclerview(){
        GridLayoutManager layoutManager = new GridLayoutManager(getmContext(),2);
        galaryinfoRv.setLayoutManager(layoutManager);
        adapter = new GalaryInfoAdapter(getmContext(),imagePaths,images);
        galaryinfoRv.setAdapter(adapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        galaryinfoRv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK && data != null) {
            // 判断手机系统版本号
            if (Build.VERSION.SDK_INT >= 19) {
                // 4.4及以上系统使用这个方法处理图片
                handleImageOnKitKat(data);
            } else {
                // 4.4以下系统使用这个方法处理图片
                handleImageBeforeKitKat(data);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        loadData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ginfo_menu,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_gname:
                upGnameDialog();
                break;
            case R.id.delete_galary:
                delete();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void upGnameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext,R.layout.up_gname_dialog,null);
        builder.setView(view,0,0,0,0);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        final EditText upEt = (EditText) view.findViewById(R.id.update_gname);
        Button cancel = (Button) view.findViewById(R.id.up_cancel);
        Button done = (Button) view.findViewById(R.id.up_done);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                imagePb.setVisibility(View.VISIBLE);
                GalaryDao.updateRow("galaryname", upEt.getText().toString(), gid, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            Toast.makeText(mContext, "修改相册名称成功", Toast.LENGTH_SHORT).show();
                            giTitleTv.setText(upEt.getText().toString());
                        }else {
                            Toast.makeText(mContext, "修改相册名称失败", Toast.LENGTH_SHORT).show();
                            Log.e(TAG,"update gname:"+e.getMessage());
                        }
                        imagePb.setVisibility(View.INVISIBLE);

                    }
                });
            }
        });

    }

    private void delete(){
        ImageDao.queryImagesInfo(gid,Preferences.getCoupleId(),  new OnCompleteListener<List<Image>>() {
            @Override
            public void onSuccess(List<Image> result) {
                List<Image> list = result;
                if (list == null || list.size() == 0){
                    deleteGalary();
                    return;
                }
                List<BmobObject> o = new ArrayList<BmobObject>();
                for (Image image : list) {
                    Image c = new Image();
                    c.setObjectId(image.getObjectId());
                    o.add(c);
                }
                ImageDao.deleteImages(o, new QueryListListener<BatchResult>() {
                    @Override
                    public void done(List<BatchResult> list, BmobException e) {
                        if(e==null){
                            deleteGalary();
                        }else{
                            if (e.getErrorCode() == 9010){
                                Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                            }else if (e.getErrorCode() == 9016){
                                Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                            }
                            Log.e("deleteImages","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }

            @Override
            public void onFailure(BmobException e) {
                if (e.getErrorCode() == 9010){
                    Toast.makeText(mContext, "网络超时", Toast.LENGTH_SHORT).show();
                }else if (e.getErrorCode() == 9016){
                    Toast.makeText(mContext, "无网络连接，请检查您的手机网络.", Toast.LENGTH_SHORT).show();
                }
                Log.e("queryImagesInfo","失败："+e.getMessage());
            }

        });
    }

    private void deleteGalary(){
        GalaryDao.deleteRow(gid, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    Toast.makeText(mContext, "删除相册成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(mContext,GalaryActivity.class));
                    finish();

                }else {
                    Toast.makeText(mContext, "删除相册失败", Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"exception:"+e.getMessage());
                }

            }
        });
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            uploadToBomb(imagePath);
            Log.e(TAG,"image:"+imagePath);


        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadToBomb(String picPath) {
        final BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    Log.e(TAG,"上传文件成功:" + bmobFile.getFileUrl());
                //写入照片表
                Image image = new Image(gid, cid,
                        bmobFile.getFileUrl());
                image.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            Log.e(TAG, "写入照片表success");
                            //更新相册表
                            updateGalary();
                        }else {
                            Log.e(TAG, "写入照片表fail");
                        }
                    }
                });
                }else{
                    Log.e(TAG,"上传文件失败：" + e.getMessage());
                }

            }

            @Override
            public void onProgress(Integer value) {
                Log.d(TAG,"上传百分比："+value);
            }
        });
    }

    private void updateGalary() {
        Galary galary = new Galary();
        galary.setImagenum(++imageCount);
        galary.update(gid, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e(TAG, "更新相册表success");
                    loadData();
                }
                else {
                    Log.e(TAG, "更新相册表fail");
                    imagePb.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    @OnClick(R.id.upload_pic_btn)
    public void uploadImage(){
        //调用相册
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }



    private void onSuccessDone(){
        imagePb.setVisibility(View.INVISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

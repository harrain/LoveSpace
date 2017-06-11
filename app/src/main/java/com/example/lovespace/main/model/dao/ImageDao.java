package com.example.lovespace.main.model.dao;

import android.util.Log;

import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.main.model.bean.Image;

import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stephen on 2017/6/3.
 */

public class ImageDao {

    private static String TAG = "ImageDao";

    public static void addToBmob(String gid, String cid, String url, final OnCompleteListener<Boolean> listener){
        Image image = new Image(gid,cid,url);
        image.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Log.e(TAG,"添加数据成功，返回objectId为"+objectId);
                    listener.onSuccess(true);
                }else{
                    Log.e(TAG,"创建数据失败" + e.getMessage());
                    listener.onFailure(e);
                }
            }
        });
    }

    public static void queryImagesInfo(String gid, String cid, final OnCompleteListener<List<Image>> listener){

        //final List<Image> images = new ArrayList<>();

        String bql ="select * from Image where galaryid = '"+gid+"' and where coupleid = '"+cid+"'";//查询所有的游戏得分记录
        BmobQuery<Image> query = new BmobQuery<Image>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.doSQLQuery(bql,new SQLQueryListener<Image>(){

            @Override
            public void done(BmobQueryResult<Image> result, BmobException e) {
                if(e ==null){
                    List<Image> list = (List<Image>) result.getResults();
                    if(list!=null && list.size()>0){
                        //images.addAll(list);
                        listener.onSuccess(list);
                    }else{
                        Log.e(TAG, "查询成功，无数据返回");
                        listener.onFailure(e);
                    }
                }else{
                    listener.onFailure(e);
                    Log.e(TAG, "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });
        //return images.size() == 0?null:images;
    }

    public static void deleteRow(String objectId, UpdateListener listener){
        Image image = new Image();
        image.setObjectId(objectId);
        image.delete(objectId,listener);
    }

    public static void deleteImages(List<BmobObject> list, QueryListListener<BatchResult> listener){
        new BmobBatch().deleteBatch(list).doBatch(listener);
    }
}

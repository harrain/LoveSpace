package com.example.lovespace.main.model.dao;

import android.util.Log;

import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.main.model.bean.Anni;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 包名：
 * 类名：
 * 类功能：纪念日数据操作类 增删改查
 * 创建者：
 * 创建日期：
 */

public class AnniDao {

    private static String TAG = "AnniDao";

    public static void fetchAnnis(String cid, final OnCompleteListener<List<Anni>> listener){
        String bql = "select * from Anni where coupleid = '"+cid+"'";
        BmobQuery<Anni> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.doSQLQuery(bql, new SQLQueryListener<Anni>() {
            @Override
            public void done(BmobQueryResult<Anni> result, BmobException e) {
                if(e ==null){
                    List<Anni> list = result.getResults();
                    if(list!=null && list.size()>0){
                        listener.onSuccess(list);
                    }else{
                        Log.e(TAG, "查询成功，无数据返回");
                        listener.onFailure(e);
                    }
                }else{
                    Log.e(TAG, "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                    listener.onFailure(e);
                }
            }
        });
    }

    public static void addToBmob(String name, String annidate, String alarmdate, String alarmtime, String coupleid, SaveListener<String> listener){
        Anni anni = new Anni(name,annidate,alarmdate,alarmtime,coupleid);
        anni.save(listener);
    }

    public static void deleteRow(String objectId, UpdateListener listener){
        Anni anni = new Anni();
        anni.setObjectId(objectId);
        anni.delete(objectId,listener);
    }

    public static void updateRow(String row,String value, String objectId,UpdateListener listener){
        Anni anni = new Anni();
        anni.setValue(row,value);
        anni.update(objectId, listener);
    }

    public static void updateAnni(String name, String annidate,  String coupleid,String objectId,UpdateListener listener){
        Anni anni = new Anni();
        anni.setValue("anniname",name);
        anni.setValue("annidate",annidate);
        anni.setValue("coupleid",coupleid);

        anni.update(objectId, listener);
    }

}

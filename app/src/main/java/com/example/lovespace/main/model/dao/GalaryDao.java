package com.example.lovespace.main.model.dao;

import android.util.Log;

import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.main.model.bean.Galary;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stephen on 2017/6/3.
 */

public class GalaryDao {

    private static String TAG = "GalaryDao";

    public static void addToBomb(String name, String cid, final OnCompleteListener<Boolean> listener){
        //final List<Boolean> flags = new ArrayList<>();
        Galary galary = new Galary(name,cid);
        galary.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    //flags.add(true);
                    Log.e(TAG,"添加数据成功，返回objectId为"+objectId);
                    listener.onSuccess(true);
                }else{
                    //flags.add(false);
                    Log.e(TAG,"创建数据失败" + e.getMessage());
                    listener.onFailure(e);
                }
            }
        });
        //return flags.size() > 0?flags.get(0):false;
    }

    public static void queryGalaryInfo(String gname, String cid, final OnCompleteListener<Galary> listener){

        //final List<Galary> galarys = new ArrayList<>();

        String bql ="select * from Galary where galaryname = '"+gname+"' and where coupleid = '"+cid+"'";//查询所有的游戏得分记录
        BmobQuery<Galary> query = new BmobQuery<Galary>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
         query.doSQLQuery(bql,new SQLQueryListener<Galary>(){

            @Override
            public void done(BmobQueryResult<Galary> result, BmobException e) {
                if(e ==null){
                    List<Galary> list = (List<Galary>) result.getResults();
                    if(list!=null && list.size()>0){
                        //galarys.addAll(list);
                        listener.onSuccess(list.get(0));
                    }else{
                        Log.e(TAG, "查询成功，无数据返回");
                    }
                }else{
                    Log.e(TAG, "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });
        //return galarys.size() == 0?null:galarys.get(0);
    }

    public static void fetchGalarys(String cid, final OnCompleteListener<List<Galary>> listener){
        //final List<Galary> galarys = new ArrayList<>();

        String bql ="select * from Galary where coupleid = '"+cid+"'";//查询所有的游戏得分记录
        BmobQuery<Galary> query = new BmobQuery<Galary>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.doSQLQuery(bql,new SQLQueryListener<Galary>(){

            @Override
            public void done(BmobQueryResult<Galary> result, BmobException e) {
                if(e ==null){
                    List<Galary> list = (List<Galary>) result.getResults();
                    if(list!=null && list.size()>0){
                        listener.onSuccess(list);
                        //galarys.addAll(list);
                    }else{
                        listener.onFailure(e);
                        Log.e(TAG, "查询成功，无数据返回");
                    }
                }else{
                    listener.onFailure(e);
                    Log.e(TAG, "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });
        //return galarys.size() == 0?null:galarys;
    }

    public static void deleteRow(String objectId, UpdateListener listener){
        Galary galary = new Galary();
        galary.setObjectId(objectId);
        galary.delete(objectId,listener);
    }

    public static void updateRow(String row,String value, String objectId,UpdateListener listener){
        Galary galary = new Galary();
        galary.setValue(row,value);
        galary.update(objectId, listener);
    }
}

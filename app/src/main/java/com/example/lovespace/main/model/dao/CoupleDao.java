package com.example.lovespace.main.model.dao;

import com.example.lovespace.main.model.bean.Couple;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stephen on 2017/5/31.
 */

public class CoupleDao {

    public static void addToBmob(String name1,String name2,SaveListener<String> listener){
        String names = name1+","+name2;
        Couple couple = new Couple(names,name1,name2);
        couple.save(listener);
    }

    public static void deleteRow(String objectId, UpdateListener listener){
        Couple couple = new Couple();
        couple.setObjectId(objectId);
        couple.delete(objectId,listener);
    }

    public static void fetchCouple(String cid, SQLQueryListener<Couple> listener){
        String bql = "select * from Couple where objectId = '"+cid+"'";
        BmobQuery<Couple> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.doSQLQuery(bql,listener);
    }

    public static void searchCouple(String account,SQLQueryListener<Couple> listener){
        String bql = "select * from Couple where onename = '"+account+"' or where twoname = '"+account+"'";
        BmobQuery<Couple> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.doSQLQuery(bql,listener);
    }
}

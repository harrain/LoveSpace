package com.example.lovespace.main.model.dao;

import com.example.lovespace.main.model.bean.Alarm;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by stephen on 2017/6/4.
 */

public class AlarmDao {

    public static void fetchAlarms(String uid, String cid, SQLQueryListener<Alarm> listener){
        String bql = "select * from Anni where userid = '"+uid+"' or where coupleid = '"+cid+"'";
        BmobQuery<Alarm> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.doSQLQuery(bql,listener);

    }

    public static void addToBmob(String name, String time, String uid, String coupleid, SaveListener<String> listener){
        Alarm alarm = new Alarm(name,time,coupleid,uid,false);
        alarm.save(listener);
    }
}

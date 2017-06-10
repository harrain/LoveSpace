package com.example.lovespace.main.model.dao;

import com.example.lovespace.main.model.bean.Alarm;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stephen on 2017/6/4.
 */

public class AlarmDao {

    public static void fetchAlarms(String uid, String cid, SQLQueryListener<Alarm> listener){
        String bql = "select * from Alarm where userid = '"+uid+"' or where coupleid = '"+cid+"'";
        BmobQuery<Alarm> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.doSQLQuery(bql,listener);

    }

    public static void addToBmob(String name, String time, String uid, String coupleid, SaveListener<String> listener){
        Alarm alarm = new Alarm(name,time,coupleid,uid,false);
        alarm.save(listener);
    }

    public static void deleteRow(String objectId, UpdateListener listener){
        Alarm alarm = new Alarm();
        alarm.setObjectId(objectId);
        alarm.delete(objectId,listener);
    }

    public static void updateRow(String row,boolean value, String objectId,UpdateListener listener){
        Alarm alarm = new Alarm();
        alarm.setValue(row,value);
        alarm.update(objectId, listener);
    }

    public static void updateAlarm(String name, String time, String uid, String coupleid,String objectId,UpdateListener listener){
        Alarm alarm = new Alarm();
        alarm.setValue("alarmname",name);
        alarm.setValue("alarmtime",time);
        alarm.setValue("userid",uid);
        alarm.setValue("coupleid",coupleid);

        alarm.update(objectId, listener);
    }



}

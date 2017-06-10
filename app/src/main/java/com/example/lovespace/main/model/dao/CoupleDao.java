package com.example.lovespace.main.model.dao;

import com.example.lovespace.main.model.bean.Couple;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stephen on 2017/5/31.
 */

public class CoupleDao {

    public static void addToBmob(String name1,String name2,SaveListener<String> listener){
        String names = name1+","+name2;
        Couple couple = new Couple(names);
        couple.save(listener);
    }

    public static void deleteRow(String objectId, UpdateListener listener){
        Couple couple = new Couple();
        couple.setObjectId(objectId);
        couple.delete(objectId,listener);
    }
}

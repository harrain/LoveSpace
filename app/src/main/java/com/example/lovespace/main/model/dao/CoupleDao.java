package com.example.lovespace.main.model.dao;

import android.util.Log;

import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.main.model.bean.Couple;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stephen on 2017/5/31.
 */

public class CoupleDao {

    public static void addToBmob(){

        Couple couple = new Couple();
        String girlname = Preferences.getOtherAccount();
        //if (!TextUtils.isEmpty(girlname))
        couple.setGirlname(girlname);
        couple.setBoyname(Preferences.getUserAccount());
        couple.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Log.e("添加数据成功，返回objectId为",""+objectId);
                }else{
                    Log.e("创建数据失败" , e.getMessage());
                }
            }
        });
    }

    public static void deleteRow(String objectId, UpdateListener listener){
        Couple couple = new Couple();
        couple.setObjectId(objectId);
        couple.delete(objectId,listener);
    }
}

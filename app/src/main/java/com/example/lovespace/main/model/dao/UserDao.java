package com.example.lovespace.main.model.dao;

import android.util.Log;

import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.listener.OnCompleteListener;
import com.example.lovespace.main.model.bean.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by stephen on 2017/6/3.
 */

public class UserDao {

    private static String TAG = "UserDao";

    public static void addToBomb(String name, String nick, String pass, String sex, Date birth, String cid){
        User user = new User(name,nick,pass,sex,birth,cid);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    Log.e("添加数据成功，返回objectId为",""+objectId);
                    Preferences.saveUserId(objectId);
                }else{
                    Log.e("创建数据失败" , e.getMessage());
                }
            }
        });
    }

    public static void queryUserInfo(String username, final OnCompleteListener<List<User>> listener){

        //final List<User> users = new ArrayList<>();

        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存获取数据，如果没有，再从网络获取。
        bmobQuery.addWhereEqualTo("username",username);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> objects, BmobException e) {
                if(e==null){
                    Log.e(TAG,"查询成功：共"+objects.size()+"条数据。");
                    if (objects.size() > 0) {
                        //users.addAll(objects);
                        listener.onSuccess(objects);
                    }
                }else{
                    listener.onFailure(e);
                    Log.e(TAG,"查询失败："+e.getMessage());
                }
            }

        });
        //return users.size() == 0?null:users.get(0);
    }

    public static User getUserInfo(String objectId){
        final List<User> users = new ArrayList<>();

        BmobQuery<User> query = new BmobQuery<User>();
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.getObject(objectId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (user != null){
                    users.add(user);
                }else {
                    Log.e(TAG,"error:"+e.getMessage());
                }
            }
        });
        return users.size() == 0?null:users.get(0);
    }
}
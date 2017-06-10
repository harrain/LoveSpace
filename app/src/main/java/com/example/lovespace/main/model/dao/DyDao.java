package com.example.lovespace.main.model.dao;

import com.example.lovespace.main.model.bean.Commit;
import com.example.lovespace.main.model.bean.Dynamics;

import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stephen on 2017/6/10.
 */

public class DyDao {

    public static void fetchDy(String cid , SQLQueryListener<Dynamics> listener){
        String bql = "select * from Dynamics where cid = '"+cid+"'";
        BmobQuery<Dynamics> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.doSQLQuery(bql,listener);
    }

    public static void fetchCommit(String cid ,String dyid, SQLQueryListener<Commit> listener){
        String bql = "select * from Commit where cid = '"+cid+"'"+ " and where dyid = '"+dyid+"'";
        BmobQuery<Commit> query = new BmobQuery<>();
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.doSQLQuery(bql,listener);
    }

    public static void addDy(String content, String uid, String cid, SaveListener<String> listener){
        Dynamics dynamics = new Dynamics(content,uid,cid);
        dynamics.save(listener);
    }

    public static void addCommit(String content, String dyid,String uid, String cid, SaveListener<String> listener){
        Commit commit = new Commit(content,dyid,uid,cid);
        commit.save(listener);
    }

    public static void deleteDy(String objectId, UpdateListener listener){
        Dynamics dynamics = new Dynamics();
        dynamics.setObjectId(objectId);
        dynamics.delete(objectId,listener);
    }

    public static void deleteCommit(String objectId, UpdateListener listener){
        Commit commit = new Commit();
        commit.setObjectId(objectId);
        commit.delete(objectId,listener);
    }

    public static void deleteCommits(List<BmobObject> list, QueryListListener<BatchResult> listener){
        new BmobBatch().deleteBatch(list).doBatch(listener);
    }
}

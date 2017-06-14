package com.example.lovespace.main.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by stephen on 2017/6/14.
 */

public class Cover extends BmobObject {

    private String coverpath;
    private String userid;

    public Cover(){}

    public Cover(String path,String uid){
        coverpath = path;
        userid = uid;
    }

    public String getCoverpath() {
        return coverpath;
    }

    public void setCoverpath(String coverpath) {
        this.coverpath = coverpath;
    }

    @Override
    public String toString() {
        return "Cover{" +
                "objectId='" + getObjectId() + '\'' +
                "userid='" + userid + '\'' +
                "coverpath='" + coverpath + '\'' +
                "createdAt='" + getCreatedAt() + '\'' +
                '}';
    }
}

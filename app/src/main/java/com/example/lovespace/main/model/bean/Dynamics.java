package com.example.lovespace.main.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by stephen on 2017/6/10.
 */

public class Dynamics extends BmobObject {

    String content;
    String uid;
    String cid;

    public Dynamics() {}

    public Dynamics(String content, String uid, String cid){
        this.content = content;
        this.uid = uid;
        this.cid = cid;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "Dynamics{" +
                "objectid='"+getObjectId()+'\'' +
                "content='" + content + '\'' +
                ", uid='" + uid + '\'' +
                ", cid='" + cid + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}

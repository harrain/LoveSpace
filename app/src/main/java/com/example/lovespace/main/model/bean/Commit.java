package com.example.lovespace.main.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by stephen on 2017/6/10.
 */

public class Commit extends BmobObject {

    String content;
    String dyid;
    String uid;
    String cid;

    public Commit() {}

    public Commit(String content, String dyid, String uid, String cid) {
        this.content = content;
        this.dyid = dyid;
        this.uid = uid;
        this.cid = cid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDyid() {
        return dyid;
    }

    public void setDyid(String dyid) {
        this.dyid = dyid;
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
        return "Commit{" +
                "objectid='"+getObjectId()+'\'' +
                "content='" + content + '\'' +
                ", dyid='" + dyid + '\'' +
                ", uid='" + uid + '\'' +
                ", cid='" + cid + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}

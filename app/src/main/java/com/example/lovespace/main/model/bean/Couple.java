package com.example.lovespace.main.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by stephen on 2017/5/31.
 */

public class Couple extends BmobObject {

    private String couplenames;

    public Couple(){}

    public Couple(String couplenames) {
        this.couplenames = couplenames;
    }

    public String getCouplenames() {
        return couplenames;
    }

    public void setCouplenames(String couplenames) {
        this.couplenames = couplenames;
    }

    @Override
    public String toString() {
        return "Couple{" +
                "objectId='" + getObjectId() + '\'' +
                ", couplenames='" + couplenames + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}

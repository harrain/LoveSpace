package com.example.lovespace.main.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by stephen on 2017/5/31.
 */

public class Couple extends BmobObject {

    private String couplenames;
    private String onename;
    private String twoname;

    public Couple(){}

    public Couple(String couplenames) {
        this.couplenames = couplenames;
    }

    public Couple(String couplenames,String onename,String twoname){
        this.couplenames = couplenames;
        this.onename = onename;
        this.twoname = twoname;
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
                ", onename='" + onename + '\'' +
                ", twoname='" + twoname + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}

package com.example.lovespace.main.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by stephen on 2017/6/3.
 */

public class Galary extends BmobObject {

    private String galaryname;
    private int imagenum;
    private String coupleid;

    public Galary(){}

    public Galary(String name,String cid){
        galaryname = name;
        coupleid = cid;
    }



    public String getGalaryname() {
        return galaryname;
    }

    public void setGalaryname(String galaryname) {
        this.galaryname = galaryname;
    }

    public int getImagenum() {
        return imagenum;
    }

    public void setImagenum(int imagenum) {
        this.imagenum = imagenum;
    }

    public String getCoupleid() {
        return coupleid;
    }

    public void setCoupleid(String coupleid) {
        this.coupleid = coupleid;
    }


    @Override
    public String toString() {
        return "Galary{" +
                "objectId='" + getObjectId() + '\'' +
                ", galaryname='" + galaryname + '\'' +
                ", imagenum=" + imagenum +
                ", coupleid='" + coupleid + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}

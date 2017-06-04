package com.example.lovespace.main.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by stephen on 2017/6/3.
 */

public class Image extends BmobObject {

    private String imageurl;
    private String galaryid;
    private String coupleid;

    public Image(){}

    public Image(String gid,String cid,String url){
        imageurl = url;
        galaryid = gid;
        coupleid = cid;
    }



    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getGalaryid() {
        return galaryid;
    }

    public void setGalaryid(String galaryid) {
        this.galaryid = galaryid;
    }

    public String getCoupleid() {
        return coupleid;
    }

    public void setCoupleid(String coupleid) {
        this.coupleid = coupleid;
    }




    @Override
    public String toString() {
        return "Image{" +
                "objectId='" + getObjectId() + '\'' +
                ", imageurl='" + imageurl + '\'' +
                ", galaryid='" + galaryid + '\'' +
                ", coupleid='" + coupleid + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}

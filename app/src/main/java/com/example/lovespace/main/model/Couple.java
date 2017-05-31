package com.example.lovespace.main.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by stephen on 2017/5/31.
 */

public class Couple extends BmobObject {

    private String girlname;

    private String boyname;

    public String getGirlname() {
        return girlname;
    }

    public void setGirlname(String girlname) {
        this.girlname = girlname;
    }

    public String getBoyname() {
        return boyname;
    }

    public void setBoyname(String boyname) {
        this.boyname = boyname;
    }
}

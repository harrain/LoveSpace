package com.example.lovespace.main.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by CDLX on 2017/6/1.
 */

public class Anni extends BmobObject{

    private String anniname;
    private String annidate;
    private String alarmdate;
    private String alarmtime;
    private String coupleid;

    public Anni(){}

    public Anni(String name,String annidate,String alarmdate,String alarmtime,String coupleid){
        anniname = name;
        this.annidate = annidate;
        this.alarmdate = alarmdate;
        this.alarmtime = alarmtime;
        this.coupleid = coupleid;
    }

    public String getAnniname() {
        return anniname;
    }

    public void setAnniname(String anniname) {
        this.anniname = anniname;
    }

    public String getAnnidate() {
        return annidate;
    }

    public void setAnnidate(String annidate) {
        this.annidate = annidate;
    }

    public String getAlarmdate() {
        return alarmdate;
    }

    public void setAlarmdate(String alarmdate) {
        this.alarmdate = alarmdate;
    }

    public String getAlarmtime() {
        return alarmtime;
    }

    public void setAlarmtime(String alarmtime) {
        this.alarmtime = alarmtime;
    }

    public String getCoupleid() {
        return coupleid;
    }

    public void setCoupleid(String coupleid) {
        this.coupleid = coupleid;
    }

    @Override
    public String toString() {
        return "Anni{" +
                "objectId='" + getObjectId() + '\'' +
                "anniname='" + anniname + '\'' +
                ", annidate='" + annidate + '\'' +
                ", alarmdate='" + alarmdate + '\'' +
                ", alarmtime='" + alarmtime + '\'' +
                ", coupleid='" + coupleid + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}

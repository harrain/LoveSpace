package com.example.lovespace.main.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by stephen on 2017/6/4.
 */

public class Alarm extends BmobObject {

    private String userid;
    private String coupleid;
    private String alarmname;
    private String alarmtime;
    private Boolean isClose;
    private Integer aid;

    public Alarm(){}
    public Alarm(String name,String time,String cid,String uid,Boolean isClose ){
        alarmname = name;
        alarmtime = time;
        coupleid = cid;
        userid = uid;
        this.isClose = isClose;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCoupleid() {
        return coupleid;
    }

    public void setCoupleid(String coupleid) {
        this.coupleid = coupleid;
    }

    public String getAlarmname() {
        return alarmname;
    }

    public void setAlarmname(String alarmname) {
        this.alarmname = alarmname;
    }

    public String getAlarmtime() {
        return alarmtime;
    }

    public void setAlarmtime(String alarmtime) {
        this.alarmtime = alarmtime;
    }

    public Boolean getClose() {
        return isClose;
    }

    public void setClose(Boolean close) {
        isClose = close;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "objectid='"+getObjectId()+'\'' +
                "userid='" + userid + '\'' +
                ", coupleid='" + coupleid + '\'' +
                ", alarmname='" + alarmname + '\'' +
                ", alarmtime='" + alarmtime + '\'' +
                ", isClose ='" + isClose + '\'' +
                ", aid = '"  + aid          +'\'' +
                '}';
    }
}

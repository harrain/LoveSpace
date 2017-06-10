package com.example.lovespace.main.model.bean;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by stephen on 2017/6/3.
 */

public class User extends BmobObject {


    private String username;
    private String nickname;
    private String password;
    private String sex;
    private Date birth;
    private String coupleid;


    public User(){}

    public User(String name,String nick,String pass,String sex,Date birth,String cid){
        username = name;
        nickname = nick;
        password = pass;
        this.sex = sex;
        this.birth = birth;
        coupleid = cid;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getCoupleid() {
        return coupleid;
    }

    public void setCoupleid(String coupleid) {
        this.coupleid = coupleid;
    }



    @Override
    public String toString() {
        return "User{" +
                "objectId='" + getObjectId() + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", birth=" + birth +
                ", coupleid='" + coupleid + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}

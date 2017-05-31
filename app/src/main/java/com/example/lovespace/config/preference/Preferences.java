package com.example.lovespace.config.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lovespace.DemoCache;

/**
 * Created by stephen on 2017/4/22.
 */

public class Preferences {

    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KEY_USER_TOKEN = "token";
    private static final String KEY_OTHER = "other";

    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    public static void saveOtherAccount(String account) {
        saveString(KEY_OTHER, account);
    }

    public static String getOtherAccount() {
        return getString(KEY_OTHER);
    }

    public static void clear(){
        saveUserAccount("");
        saveUserToken("");
        saveOtherAccount("");

    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }



    static SharedPreferences getSharedPreferences() {
        return DemoCache.getContext().getSharedPreferences("lovespace_user", Context.MODE_PRIVATE);
    }

    /*static SharedPreferences getSharedPreferences() {
        return DemoCache.getContext().getSharedPreferences("lovespace."+DemoCache.getAccount(), Context.MODE_PRIVATE);
    }*/
}

package com.example.lovespace.config.preference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.lovespace.DemoCache;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;

import org.json.JSONObject;

/**
 * 包名： com.example.lovespace.config.preference
 * 类名:  UserPreferences
 * 类功能：
 * 创建者：  stephen
 * 创建日期: 2017/4/29
 */
public class UserPreferences {

    private final static String KEY_STATUS_BAR_NOTIFICATION_CONFIG = "KEY_STATUS_BAR_NOTIFICATION_CONFIG";
    private final static String KEY_SB_NOTIFY_TOGGLE = "sb_notify_toggle";
    // 测试过滤通知
    private final static String KEY_MSG_IGNORE = "KEY_MSG_IGNORE";
    // 响铃配置
    private final static String KEY_RING_TOGGLE = "KEY_RING_TOGGLE";
    // 呼吸灯配置
    private final static String KEY_LED_TOGGLE = "KEY_LED_TOGGLE";
    // 通知栏标题配置
    private final static String KEY_NOTICE_CONTENT_TOGGLE = "KEY_NOTICE_CONTENT_TOGGLE";

    // 通知栏样式（展开、折叠）配置
    private final static String KEY_NOTIFICATION_FOLDED_TOGGLE = "KEY_NOTIFICATION_FOLDED";

    public static void setMsgIgnore(boolean enable) {
        saveBoolean(KEY_MSG_IGNORE, enable);
    }

    public static boolean getNotificationToggle() {
        return getBoolean(KEY_SB_NOTIFY_TOGGLE, true);
    }

    public static boolean getMsgIgnore() {
        return getBoolean(KEY_MSG_IGNORE, false);
    }

    private static boolean getBoolean(String key, boolean value) {
        return getSharedPreferences().getBoolean(key, value);
    }

    private static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static StatusBarNotificationConfig getStatusConfig() {
        return getConfig(KEY_STATUS_BAR_NOTIFICATION_CONFIG);
    }

    private static  StatusBarNotificationConfig getConfig(String key){
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        String jsonString = getSharedPreferences().getString(key,"");
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject == null){
                return null;
            }
            config.notificationSmallIconId = jsonObject.getInt("notificationSmallIconId");
            config.notificationSound = jsonObject.getString("notificationSound");
            config.ledARGB = jsonObject.getInt("ledargb");
            config.ledOnMs = jsonObject.getInt("ledonms");
            config.ledOffMs = jsonObject.getInt("ledoffms");
            config.notificationEntrance = (Class<? extends Activity>) Class.forName(jsonObject.getString("notificationEntrance"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return config;

    }
    private static SharedPreferences getSharedPreferences(){
        return DemoCache.getContext().getSharedPreferences("Demo."+DemoCache.getAccount(), Context.MODE_PRIVATE);
    }

    public static void setStatusConfig(StatusBarNotificationConfig config) {
        saveStatusBarNotificationConfig(KEY_STATUS_BAR_NOTIFICATION_CONFIG, config);
    }
    private static void saveStatusBarNotificationConfig(String key,StatusBarNotificationConfig config){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("downTimeBegin", config.downTimeBegin);
            jsonObject.put("downTimeEnd", config.downTimeEnd);
            jsonObject.put("downTimeToggle", config.downTimeToggle);
            jsonObject.put("ring", config.ring);
            jsonObject.put("vibrate", config.vibrate);
            jsonObject.put("notificationSmallIconId", config.notificationSmallIconId);
            jsonObject.put("notificationSound", config.notificationSound);
            jsonObject.put("hideContent", config.hideContent);
            jsonObject.put("ledargb", config.ledARGB);
            jsonObject.put("ledonms", config.ledOnMs);
            jsonObject.put("ledoffms", config.ledOffMs);
            jsonObject.put("titleOnlyShowAppName", config.titleOnlyShowAppName);
            jsonObject.put("notificationEntrance", config.notificationEntrance.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.putString(key, jsonObject.toString());
        editor.commit();
    }
}

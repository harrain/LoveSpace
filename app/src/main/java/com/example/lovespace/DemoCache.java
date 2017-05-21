package com.example.lovespace;

import android.content.Context;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;

/**
 * 包名： com.example.lovespace
 * 类名:  DemoCache
 * 类功能：
 * 创建者：  stephen
 * 创建日期: 2017/4/22
 */
public class DemoCache {

    private static Context context;
    private static String account ;
    private static StatusBarNotificationConfig notificationConfig;

    public static void setAccount(String account){
        DemoCache.account = account;
        NimUIKit.setAccount(account);
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        DemoCache.context = context;
    }

    public static void setNotificationConfig(StatusBarNotificationConfig config) {
        DemoCache.notificationConfig = config;
    }

    public static StatusBarNotificationConfig getNotificationConfig() {
        return notificationConfig;
    }

    public static String getAccount() {
        return account;
    }


    public static void clear() {
        account = null;
    }
}

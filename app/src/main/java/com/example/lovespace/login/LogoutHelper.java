package com.example.lovespace.login;/**
 * Created by stephen on 2017/5/13.
 */

import com.example.lovespace.DemoCache;
import com.example.lovespace.config.preference.Preferences;
import com.netease.nim.uikit.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.NimUIKit;

/**
 * 包名： com.example.lovespace.login
 * 类名:  LogoutHelper
 * 类功能：注销帮助类
 * 创建者：  stephen
 * 创建日期: 2017/5/13
 */
public class LogoutHelper {

    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NimUIKit.clearCache();
        DemoCache.clear();
        Preferences.clear();
        LoginSyncDataStatusObserver.getInstance().reset();

        //DropManager.getInstance().destroy();
    }
}

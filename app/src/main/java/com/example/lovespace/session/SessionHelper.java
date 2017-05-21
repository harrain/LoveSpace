package com.example.lovespace.session;/**
 * Created by stephen on 2017/5/1.
 */

import android.content.Context;

import com.example.lovespace.DemoCache;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.session.SessionCustomization;
import com.netease.nimlib.sdk.msg.model.IMMessage;



/**
 * 包名： com.example.lovespace.session
 * 类名:  SessionHelper
 * 类功能：UIKit自定义消息界面用法展示类
 * 创建者：  stephen
 * 创建日期: 2017/5/1
 */
public class SessionHelper {

    private static SessionCustomization p2pCustomization;

    public static void init(){
        // 注册自定义消息附件解析器
        //NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());


        NimUIKit.setCommonP2PSessionCustomization(getP2pCustomization());
    }

    public static void startP2PSession(Context context, String account) {
        startP2PSession(context, account, null);
    }

    //开始单聊
    public static void startP2PSession(Context context, String account, IMMessage anchor) {
        if (!DemoCache.getAccount().equals(account)) {
            NimUIKit.startP2PSession(context, account, anchor);
        }
    }

    private static SessionCustomization getP2pCustomization(){
        return null;
    }
}

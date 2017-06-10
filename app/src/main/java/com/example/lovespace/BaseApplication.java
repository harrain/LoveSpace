package com.example.lovespace;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.example.lovespace.common.util.sys.SystemUtil;
import com.example.lovespace.config.preference.Preferences;
import com.example.lovespace.config.preference.UserPreferences;
import com.example.lovespace.main.activity.WelcomeActivity;
import com.example.lovespace.session.NimDemoLocationProvider;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.contact.core.query.PinYin;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.IMMessageFilter;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;

import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by stephen on 2017/4/22.
 */

public class BaseApplication extends Application {

    private static final String BOMB_APPCATION_ID = "27bc225df548d8d318d4e751c7e5b0ed";

    private static Application instance;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initBomb();
        DemoCache.setContext(this);
        // 注册小米推送appID 、appKey 以及在云信管理后台添加的小米推送证书名称，该逻辑放在 NIMClient init 之前
        NIMPushClient.registerMiPush(this, "DEMOMIPUSH", "2882303761517573419", "5591757313419");
        NIMClient.init(this,getLoginInfo(),getOptions());

        if (inMainProcess()){
            PinYin.init(this);
            try {
                PinYin.validate();
            }catch (Exception e){
                e.printStackTrace();
            }

            // 初始化UIKit模块
            initUIKit();

            // 注册通知消息过滤器
            registerIMMessageFilter();
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        }
    }

    private void initBomb(){
        BmobConfig config =new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId(BOMB_APPCATION_ID)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)){
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account,token);
        }else {
            return null;
        }
    }

    private void initUIKit(){
        // 初始化，使用 uikit 默认的用户信息提供者
        NimUIKit.init(this);

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());
    }

    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();
        //新消息通知托管给SDK
        initStatusBarNotificationConfig(options);
        // 配置保存图片，文件，log等数据的目录
        String sdkPath = Environment.getExternalStorageDirectory()+"/"+getPackageName()+"/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";
        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;


        return options;
    }

    private void initStatusBarNotificationConfig(SDKOptions options){
        //加载应用的状态栏设置
        StatusBarNotificationConfig config = loadStatusBarNotificationConfig();
        //加载用户的StatusBarNotificationConfig
        StatusBarNotificationConfig userConfig = UserPreferences.getStatusConfig();
        if (userConfig == null){
            userConfig = config;
        }else {
            // 新增的 UserPreferences 存储项更新，兼容 3.4 及以前版本
            // APP默认 StatusBarNotificationConfig 配置修改后，使其生效
            userConfig.notificationEntrance = config.notificationEntrance;

        }
        // 持久化生效
        UserPreferences.setStatusConfig(config);
        // SDK statusBarNotificationConfig 生效
        options.statusBarNotificationConfig = userConfig;

    }

    /**
     * 可以自定义初始的StatusBarNotificationConfig
     * @return
     */
    private StatusBarNotificationConfig loadStatusBarNotificationConfig(){
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        //点击通知跳转到的界面
        config.notificationEntrance = WelcomeActivity.class;
        config.notificationSmallIconId = R.mipmap.ic_stat_notify_msg;
        //通知铃声uri
        config.notificationSound = "android.resource://com.example.lovespace/raw/msg";
        //呼吸灯设置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        //留作切换账号备用
        DemoCache.setNotificationConfig(config);
        return config;


    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(this);
        return packageName.equals(processName);
    }

    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private void registerIMMessageFilter() {
        NIMClient.getService(MsgService.class).registerIMMessageFilter(new IMMessageFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (UserPreferences.getMsgIgnore() && message.getAttachment() != null) {
                    if (message.getAttachment() instanceof UpdateTeamAttachment) {
                        UpdateTeamAttachment attachment = (UpdateTeamAttachment) message.getAttachment();
                        for (Map.Entry<TeamFieldEnum, Object> field : attachment.getUpdatedFields().entrySet()) {
                            if (field.getKey() == TeamFieldEnum.ICON) {
                                return true;
                            }
                        }
                    } else if (message.getAttachment() instanceof AVChatAttachment) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public static Application getInstance() {
        return instance;
    }
}
